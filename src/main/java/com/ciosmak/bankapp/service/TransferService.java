package com.ciosmak.bankapp.service;

import com.ciosmak.bankapp.entity.BankAccount;
import com.ciosmak.bankapp.entity.Transfer;
import com.ciosmak.bankapp.repository.BankAccountRepository;
import com.ciosmak.bankapp.repository.TransferRepository;
import com.ciosmak.bankapp.user.id.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TransferService extends AbstractService
{
    public void create(UserId userId)
    {
        System.out.println("\n---WYKONAJ PRZELEW---");

        String title;
        System.out.print("Podaj tytuł przelewu: ");
        title = scanner.nextLine();


        BigDecimal amountOfMoneyToTransfer;
        while (true)
        {
            try
            {
                System.out.print("Podaj kwotę: ");
                amountOfMoneyToTransfer = scanner.nextBigDecimal();
                Pattern pattern = Pattern.compile("\\d+\\.(\\d{3,})");
                Matcher matcher = pattern.matcher(amountOfMoneyToTransfer.toString());
                if (isNumberNegative(amountOfMoneyToTransfer))
                {
                    throw new IllegalAccessException();
                }
                if (!matcher.find())
                {
                    break;
                }
                throw new InputMismatchException();
            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota jest błędna.\nKwota powinna się być liczbą z maksymalnie dwiema cyframi po przecinku.\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (IllegalAccessException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota jest błędna.\nKwota nie może byc ujemna.\nSpróbuj ponownie");
                System.err.flush();
            }
            catch (Exception e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Podana kwota jest błędna.\nKwota jest zbyt dużą liczbą.\nSpróbuj ponownie.");
                System.err.flush();
            }
        }

        String accountNumber;
        System.out.print("Podaj numer konta odbiorcy: ");
        while (true)
        {
            scanner = new Scanner(System.in);
            accountNumber = scanner.nextLine();

            if (checkIfAccountNumberIsCorrect(accountNumber))
            {
                break;
            }
            System.err.println("Podany numer konta jest błędny.\nNumer konta powinien się składać tylko z 26 cyfr.");
            System.err.flush();
            System.out.print("Ponownie podaj numer konta odbiorcy: ");
        }

        LocalDateTime executionDate = LocalDateTime.now();

        LocalDateTime postingDate = setPostingDate(executionDate);

        Optional<BankAccount> bankAccount = bankAccountRepository.findById(chooseOneBankAccount(userId));
        if (bankAccount.isPresent())
        {
            if (isEnoughMoneyInAccount(bankAccount.get().getBalance(), amountOfMoneyToTransfer))
            {
                bankAccount.get().setBalance(bankAccount.get().getBalance().subtract(amountOfMoneyToTransfer));
                Transfer transfer = Transfer.builder().
                        title(title).
                        amountOfMoney(amountOfMoneyToTransfer).
                        senderBankAccountNumber(bankAccount.get().getBankAccountNumber()).
                        receivingBankAccountNumber(accountNumber).
                        executionDate(executionDate).
                        postingDate(postingDate).
                        build();
                transferRepository.save(transfer);
                bankAccount.get().getTransfers().add(transfer);

                System.out.println("\n---PRZELEW ZOSTAŁ WYKONANY---");
            }
            else
            {
                System.err.println("\n---PRZELEW NIE ZOSTAŁ WYKONANY PRZEZ BRAK TAKIEJ KWOTY NA KONCIE---");
            }
        }
        else
        {
            System.err.println("BŁĄD KRYTYCZNY!!!");
            System.err.println("OPUSZCZANIE PROGRAMU");
            System.err.flush();
            System.exit(1);
        }


    }

    public void autoMakeTransfers()
    {
        ArrayList<Transfer> transfersList = transferRepository.findAll();
        for (var transfer : transfersList)
        {
            if (!transfer.isDone())
            {
                if (transfer.getPostingDate().compareTo(transfer.getExecutionDate()) < 0)
                {
                    transfer.setDone(true);
                    Optional<BankAccount> bankAccount = bankAccountRepository.findByBankAccountNumber(transfer.getReceivingBankAccountNumber());
                    bankAccount.ifPresent(account -> account.setBalance(account.getBalance().add(transfer.getAmountOfMoney())));
                }
            }
        }
    }

    private boolean checkIfAccountNumberIsCorrect(String accountNumber)
    {
        return accountNumber.matches("^[0-9]*$") && accountNumber.length() == 26;
    }

    private LocalDateTime setPostingDate(LocalDateTime executionDate)
    {
        LocalDateTime postingDate;
        if (executionDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || executionDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))
        {
            postingDate = executionDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).withHour(12).withMinute(0).withSecond(0);
        }
        else if (executionDate.getHour() < 12)
        {
            postingDate = executionDate.withHour(12).withMinute(0).withSecond(0);
        }
        else if (executionDate.getHour() < 16)
        {
            postingDate = executionDate.withHour(16).withMinute(0).withSecond(0);
        }
        else
        {
            postingDate = executionDate.plusDays(1).withHour(12).withMinute(0).withSecond(0);
        }
        return postingDate;
    }

    private boolean isEnoughMoneyInAccount(BigDecimal balance, BigDecimal amountOfMoneyToTransfer)
    {
        return balance.subtract(amountOfMoneyToTransfer).compareTo(BigDecimal.ZERO) > 0;
    }

    private Long chooseOneBankAccount(UserId userId)
    {
        ArrayList<BankAccount> bankAccountsList = bankAccountRepository.findByUserId(userId.getId());
        int amountOfBankAccounts = bankAccountsList.size();
        int selectedBankAccount;

        while (true)
        {
            try
            {
                System.out.println("\n---WYBIERZ RACHUNEK BANKOWY---");
                for (int i = 0; i < amountOfBankAccounts; ++i)
                {
                    System.out.println(i + 1 + ". " + bankAccountsList.get(i).getName());
                }
                System.out.print("Wybieram: ");
                selectedBankAccount = scanner.nextInt();
                selectedBankAccount--;
                scanner = new Scanner(System.in);
                if (checkIfCorrectProductIsSelected(selectedBankAccount, amountOfBankAccounts))
                {
                    return bankAccountsList.get(selectedBankAccount).getId();
                }
                else
                {
                    throw new InputMismatchException();
                }

            }
            catch (InputMismatchException e)
            {
                scanner = new Scanner(System.in);
                System.err.println("Nie ma takiej opcji.\nNależy wprowadzić liczbę od 1 do " + amountOfBankAccounts + ".\nSpróbuj ponownie.");
                System.err.flush();
            }
            catch (Exception e)
            {
                System.err.println("BŁĄD KRYTYCZNY!!!");
                System.err.println("OPUSZCZANIE PROGRAMU");
                System.err.flush();
                System.exit(1);
            }
        }
    }


    private final BankAccountRepository bankAccountRepository;
    private final TransferRepository transferRepository;

}
