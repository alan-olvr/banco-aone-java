import entities.*;

import java.util.*;

import exceptions.InvalidDataAccountException;
import exceptions.InvalidValueException;
import util.InterfaceMenu;
import exceptions.InsufficientBalanceException;

public class Main {

    /*
     - Runs the account operations menu in a loop until the user chooses to go back to the initial menu (option 0).
     - @param acc the account being operated on
     - @param sc the scanner used to read the user input
     - @param interfaceUser the class responsible for menus, readings and screens
     */

    public static void menuAcoesConta(Account acc, Scanner sc, InterfaceMenu interfaceUser) {
        int accountMenuOption; //option chosen in the account menu
        double value; //amount typed by the user in deposits and withdraws
        List<Transaction> list; //transactions filtered by type (deposits or withdraws)

        do {
            interfaceUser.accountMenu();
            accountMenuOption = interfaceUser.accountMenuOption(sc);

            //Option 1: keeps asking for values while the user wants to deposit again
            if(accountMenuOption == 1) {
                boolean repeat;
                do {
                    value = interfaceUser.readDepositValue(sc);
                    try {
                        acc.deposit(value);
                        System.out.printf("\n[OK]: Depósito de R$ %.2f realizado com sucesso.\n", value);
                        double rent = acc.calculateYield(value); //Yield credited by this deposit (zero when the account has no yield)
                        if (rent > 0) System.out.printf("[INFO] Rendimento creditado: R$ %.2f%n", rent);
                        System.out.printf("[INFO]: Saldo atual: R$ %.2f\n", acc.getAccountBalance());
                        repeat = interfaceUser.confirmAnswer(sc, "Deseja realizar um novo depósito?");
                    } catch (InvalidValueException e) {
                        //Invalid amount: asks for a new value right away
                        System.out.print(e.getMessage());
                        repeat = true;
                    } catch (IllegalStateException s) {
                        //Daily deposit limit reached: ends the deposit flow
                        System.out.print(s.getMessage());
                        repeat = false;
                    }
                } while (repeat);

            //Option 2: keeps asking for values while the user wants to withdraw again
            } else if(accountMenuOption == 2) {
                boolean repeat;
                do {
                    value = interfaceUser.readWithdrawValue(sc);
                    try {
                        acc.moneyWithdraw(value);
                        System.out.printf("\n[OK]: Saque de R$ %.2f realizado com sucesso.\n", value);
                        System.out.printf("[INFO]: Saldo atual: R$ %.2f\n", acc.getAccountBalance());
                        repeat = interfaceUser.confirmAnswer(sc, "Deseja realizar um novo saque?");
                    } catch (InvalidValueException e) {
                        //Invalid amount: asks for a new value right away
                        System.out.print(e.getMessage());
                        repeat = true;
                    } catch (IllegalStateException e) {
                        //Daily withdraw limit reached: ends the withdrawal flow
                        System.out.print(e.getMessage());
                        repeat = false;
                    } catch (InsufficientBalanceException e) {
                        //Amount higher than the balance: shows the balance and asks whether to try another value
                        System.out.print(e.getMessage() + "\n");
                        System.out.printf("[INFO]: Saldo atual - R$ %.2f%n", acc.getAccountBalance());
                        repeat = interfaceUser.confirmAnswer(sc, "Deseja tentar com um novo valor?");
                    }
                } while (repeat);

            //Option 3: prints every transaction (deposits and withdraws)
            } else if(accountMenuOption == 3) {
                try {
                    System.out.println();
                    acc.consultExtract();
                } catch (IllegalArgumentException e) {
                    //No transactions registered yet
                    System.out.println(e.getMessage());
                }

            //Option 4: Extract filtered by deposits
            } else if(accountMenuOption == 4) {
                try {
                    list = acc.consultExtract(TransactionType.DEPOSIT);
                    System.out.print(interfaceUser.depositsHistoric());
                    list.forEach(t -> System.out.println(t.formatOut()));
                } catch (IllegalArgumentException e) {
                    //No transactions registered yet
                    System.out.println(e.getMessage());
                }

            //Option 5: Extract filtered by withdraws
            } else if(accountMenuOption == 5) {
                try {
                    list = acc.consultExtract(TransactionType.WITHDRAW);
                    System.out.print(interfaceUser.withdrawsHistoric());
                    list.forEach(t -> System.out.println(t.formatOut()));
                } catch (IllegalArgumentException e) {
                    //No transactions registered yet
                    System.out.println(e.getMessage());
                }

            // Option 6: shows the available balance and the accumulated yield
            } else if(accountMenuOption == 6) {
                System.out.println(interfaceUser.consultBalance(acc));

            //Option 0: Leaves the account menu and goes back to the initial menu
            } else if(accountMenuOption == 0) {
                System.out.println("[OK]: Encerrando acesso...\n");

            //Any other value is not a menu option
            } else {
                System.out.println("[ERRO]: Opção inválida.\n");
            }
        } while(accountMenuOption != 0);
    }

    /*
     - Application start: runs the initial menu loop, reads the holder data,
     - creates the chosen account type and opens its operations menu.
     */

    public static void main(String[] args) {

        Locale.setDefault(Locale.US); //Makes numbers be read and printed with a dot as the decimal separator
        Scanner sc = new Scanner(System.in);
        Account acc;
        InterfaceMenu interfaceUser = new InterfaceMenu();

        int select = 1; //Loop control: the initial menu repeats while select is different from 0

        //Catches non-numeric input that was not handled inside the cases below
        try {
            do {
                interfaceUser.initialMenu();

                int option = interfaceUser.inicialMenuOption(sc);
                if (option == 3) select = 0;
                sc.nextLine(); //Consumes the line break left in the buffer after reading the option number

                switch(option) {
                    //Case1 - Checking account
                    case 1:
                        try {
                            System.out.print("[AVISO]: Para acessar sua conta corrente, insira os dados básicos necessários abaixo: \n");
                            //Holder and account data, read in this order
                            String holderName = interfaceUser.readHolderName(sc);
                            int accountNum = interfaceUser.readAccountNum(sc);
                            String holderCpf = interfaceUser.readHolderCpf(sc);
                            int holderAge = interfaceUser.readHolderAge(sc);

                            //Age 0 ends the application
                            if(holderAge == 0) {
                                select = 0;
                                break;
                            }

                            double balance = interfaceUser.readAccountBalance(sc);

                            // Initial balance 0 ends the application
                            if(balance == 0) {
                                select = 0;
                                break;
                            }

                            System.out.printf("\n [OK]: Acesso realizado. Olá, %s!\n", holderName.toUpperCase());

                            //Creates the account (the constructor validates the data) and opens its menu
                            acc = new CheckingAccount(accountNum, holderName, holderCpf, holderAge, balance);
                            menuAcoesConta(acc, sc, interfaceUser);
                            break;
                        } catch (InvalidDataAccountException | InputMismatchException e) {
                            //Invalid account data or non-numeric input: shows the message and ends the application
                            System.out.print(e.getMessage());
                            select = 0; break;
                        }
                    //Case2 - Saving account (same flow as the checking account)
                    case 2:
                        try {
                            System.out.print("[AVISO]: Para acessar sua conta poupança, insira os dados básicos necessários abaixo: \n");
                            String holderName = interfaceUser.readHolderName(sc);
                            int accountNum = interfaceUser.readAccountNum(sc);
                            String holderCpf = interfaceUser.readHolderCpf(sc);
                            int holderAge = interfaceUser.readHolderAge(sc);

                            if (holderAge == 0) {
                                select = 0;
                                break;
                            }

                            double balance = interfaceUser.readAccountBalance(sc);

                            if(balance == 0) {
                                select = 0;
                                break;
                            }

                            System.out.printf("\n [OK]: Acesso realizado. Olá, %s!\n", holderName.toUpperCase());

                            // Informs the user about the yield of savings accounts
                            System.out.print("\n [INFO]: Depósitos na poupança rendem 0,1% em depósitos.\n");

                            acc = new SavingAccount(accountNum, holderName, holderCpf, holderAge, balance);
                            menuAcoesConta(acc, sc, interfaceUser);
                            break;
                        } catch (InvalidDataAccountException | InputMismatchException e) {
                            System.out.print(e.getMessage());
                            select = 0; break;
                        }

                    // Case 3 - Logout: select was already set to 0 above, so the loop ends
                    case 3:
                        System.out.print("Obrigado por utilizar o Banco AOne. Até logo!");
                        break;

                    //Any other value is not a menu option
                    default:
                        System.out.println("[ERRO]: Opção inválida.\n   ");
                }
            } while (select != 0);
        } catch(InputMismatchException e) {
            System.out.print(e.getMessage());
        }

        //Releases the input stream
        sc.close();
    }
}
