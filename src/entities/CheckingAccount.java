package entities;

import java.time.LocalDateTime;

import static entities.TransactionType.WITHDRAW;
import exceptions.InsufficientBalanceException;

/*
 - Checking account.
 - Has no yield and no deposit limit (it uses the defaults of Account) and
 - allows a limited number of withdrawals per day.
 */
public class CheckingAccount extends Account {
    private static final int WITHDRAW_LIMIT = 4;  //Maximum number of withdrawals allowed per day

    /*
     - Creates a checking account. The data validation is done by the Account constructor.
     - @param accountNum    account number
     - @param holderName    holder name
     - @param holderCpf     holder CPF
     - @param holderAge     holder age
     - @param holderBalance initial balance
     */
    public CheckingAccount(int accountNum, String holderName, String holderCpf, int holderAge, double holderBalance){
        super(accountNum, holderName, holderCpf, holderAge, holderBalance);
    }

    /*
     - Withdraws the given value from the account.
     - Validates, in this order, the value, the daily withdraw limit and the available balance.
     - Then subtracts the value from the balance and registers a WITHDRAW transaction.
     - @param value the amount to withdraw
     - @throws exceptions.InvalidValueException  if the value is not finite or not greater than zero
     - @throws IllegalStateException             if the daily withdraw limit was already reached
     - @throws InsufficientBalanceException      if the value is greater than the current balance
     */
    @Override
    public void moneyWithdraw(double value) {
        validateValue(value);
        if(countToday(WITHDRAW) >= WITHDRAW_LIMIT) {
            throw new IllegalStateException("[ERRO]: Limite diário de saques atingido.\n");
        }
        if(value > getAccountBalance()){
            throw new InsufficientBalanceException();
        }

        setBalance(getAccountBalance() - value);
        getTransaction().add(new Transaction(WITHDRAW, LocalDateTime.now(), value));
    }

}

