package util;

import java.util.Scanner;
import entities.Account;

/*
 - Handles the command-line interface of the banking system: displays menus,
 - reads and validates user input and builds the text of the balance and history screens.
 */
public class InterfaceMenu {

    /*
     - Prints the initial menu with the options to access the checking account,
     - the savings account or to log out.
     */
    public void initialMenu(){
        System.out.print("============================================\n");
        System.out.print("                BANCO AONE          \n");
        System.out.print("============================================\n");
        System.out.print("       Olá! O que deseja fazer hoje? \n");
        System.out.println();
        System.out.print("1 - Acessar minha conta corrente.\n");
        System.out.print("2 - Acessar minha conta poupança.\n");
        System.out.print("3 - Logout.\n");
        System.out.print("--------------------------------------------\n");
    }

    /*
     - Prints the menu of operations available for the selected account:
     - deposit, withdraw, statement, deposit/withdraw history and balance.
     */
    public void accountMenu(){
        System.out.println();
        System.out.print("============================================\n");
        System.out.print("           OPERAÇÕES DISPONÍVEIS     \n");
        System.out.print("============================================\n");
        System.out.print("1 - Depositar.\n");
        System.out.print("2 - Sacar.\n");
        System.out.print("3 - Consultar extrato.\n");
        System.out.print("4 - Consultar depósitos.\n");
        System.out.print("5 - Consultar saques.\n");
        System.out.print("6 - Consultar saldo.\n");
        System.out.print("0 - Voltar ao menu inicial.\n");
        System.out.print("--------------------------------------------\n");
    }

    /*
     - Reads the option chosen by the user in the initial menu.
     - Does not consume the remaining line break from the buffer.
     - @param sc the Scanner used to read the standard input
     - @return the option number typed by the user
     */
    public int inicialMenuOption(Scanner sc) {
        System.out.print("Digite o número da opção desejada: ");
        return sc.nextInt();
    }

    /*
     - Reads the option chosen by the user in the account menu and consumes the
     - remaining line break so later nextLine() calls are not affected.
     - @param sc the Scanner used to read the standard input
     - @return the option number typed by the user
     */
    public int accountMenuOption(Scanner sc) {
        System.out.print("Digite o número da opção desejada: ");
        int selectedOption = sc.nextInt();
        sc.nextLine();
        return selectedOption;
    }

    /*
     - Reads the full name of the account holder. Keeps asking while the name is
     - blank or contains any digit.
     - @param sc the Scanner used to read the standard input
     - @return the validated holder name
     */
    public String readHolderName(Scanner sc) {
        System.out.print("Nome completo do titular: ");
        String name = sc.nextLine();

        while(containsDigit(name) || name.isBlank()) {
            System.out.print("[AVISO]: O nome do titular não pode conter dígitos ou estar vazio. Tente novamente - ");
            name = sc.nextLine();
        }

        return name;
    }

    /*
    - Reads the account number. Keeps asking while the value is outside the
    - range defined by Account.ACCOUNT_NUM_MIN and Account.ACCOUNT_NUM_MAX.
    - @param sc the Scanner used to read the standard input
    - @return the validated account number
    */
    public int readAccountNum(Scanner sc) {
        System.out.print("Número da conta: ");
        int accountNum = sc.nextInt();

        while(accountNum < Account.ACCOUNT_NUM_MIN || accountNum > Account.ACCOUNT_NUM_MAX){
            System.out.print("[AVISO]: Insira um número válido - ");
            accountNum = sc.nextInt();
        }

        sc.nextLine();
        return accountNum;
    }

    /*
    - Reads the CPF of the account holder. Accepts only digits and requires
    - exactly Account.CPF_LENGTH characters, otherwise asks again.
    - @param sc the Scanner used to read the standard input
    - @return the validated CPF (numbers only)
    */
    public String readHolderCpf(Scanner sc) {
        System.out.print("CPF (somente números): ");
        String cpfHolder = sc.nextLine();

        while(!cpfHolder.matches("\\d{" + Account.CPF_LENGTH + "}")) {
            System.out.print("[AVISO]: Insira um CPF válido: ");
            cpfHolder = sc.nextLine();
        }

        return cpfHolder;
    }


    /*
    - Reads the age of the account holder. Keeps asking while the age is below
    - the minimum allowed (Account.MIN_AGE).
    - @param sc the Scanner used to read the standard input
    - @return the validated age
    */
    public int readHolderAge(Scanner sc) {
        System.out.print("Idade do titular: ");
        int age = sc.nextInt();

        while(age < Account.MIN_AGE) {
            System.out.print("[AVISO]: Idade não permitida. Insira uma idade válida: ");
            age = sc.nextInt();
        }

        return age;
    }

    /*
     - Reads the initial balance of the account. Rejects non-finite values
     - (NaN or infinity) and negative values, asking again until it is valid.
     - @param sc the Scanner used to read the standard input
     - @return the validated initial balance
     */
    public double readAccountBalance(Scanner sc) {
        System.out.print("Saldo inicial (R$): ");
        double balanceAccount = sc.nextDouble();

        while(!Double.isFinite(balanceAccount) || balanceAccount < 0) {
            System.out.print("[AVISO]: Valor do saldo inválido. Informe um valor maior ou igual a zero: ");
            balanceAccount = sc.nextDouble();
        }

        return balanceAccount;
    }

    /*
    - Reads the amount to be deposited and consumes the remaining line break.
    - The amount itself is not validated here.
    - @param sc the Scanner used to read the standard input
    - @return the deposit amount typed by the user
    */
    public double readDepositValue(Scanner sc) {
        System.out.print("Valor do depósito (R$): ");
        double depositValue = sc.nextDouble();
        sc.nextLine();
        return depositValue;
    }

    /*
     - Reads the amount to be withdrawn and consumes the remaining line break.
     - The amount itself is not validated here (e.g. sufficient balance).
     - @param sc the Scanner used to read the standard input
     - @return the withdrawal amount typed by the user
     */
    public double readWithdrawValue(Scanner sc) {
        System.out.print("Valor do saque (R$): ");
        double withdrawValue = sc.nextDouble();
        sc.nextLine();
        return withdrawValue;
    }

    /*
     - Builds the balance screen with the holder name, account number and
     - available balance. If the account has accumulated yield greater than
     - zero, it is also included in the text.
     - @param acc the account whose balance will be displayed
     - @return the formatted text of the balance screen
     */
    public String consultBalance(Account acc) {
        String screen = String.format(
                "%n============================================%n" +
                "            CONSULTA DE SALDO%n" +
                "  Titular: %s%n" +
                "  Conta: %d%n" +
                "--------------------------------------------%n" +
                "  SALDO DISPONÍVEL: R$ %.2f%n",
                acc.getHolderName(), acc.getAccountNum(), acc.getAccountBalance()
        );

        double yield = acc.totalYield();
        if(yield > 0) {
            screen += String.format("  RENDIMENTO ACUMULADO: R$ %.2f%n", yield);
        }

        return screen;
    }

    /*
     - Returns the header of the deposit history screen. The list of deposits
     - is expected to be appended to this text by the caller.
     - @return the formatted header of the deposit history
     */
    public String depositsHistoric(){
        return """
                \n============================================
                          DEPÓSITOS REALIZADOS
                --------------------------------------------
                """;
    }

    /*
     - Returns the header of the withdrawal history screen. The list of withdraws
     - is expected to be appended to this text by the caller.
     - @return the formatted header of the withdrawal history
     */
    public String withdrawsHistoric(){
        return """
                \n============================================
                          SAQUES REALIZADOS
                --------------------------------------------
                """;
    }

    /*
     - Shows a confirmation question followed by (S/N) and reads the answer.
     - Any answer other than "S" (case-insensitive) is treated as "no".
     - @param sc the Scanner used to read the standard input
     - @param message the question displayed to the user
     - @return true if the user answered "S", false otherwise
     */
    public boolean confirmAnswer(Scanner sc, String message) {
        System.out.print(message + " (S/N): ");
        String answer = sc.next();
        return answer.equalsIgnoreCase("S");
    }

    /*
     - Checks whether a text contains at least one numeric character.
     - Static utility used to validate the holder name.
     - @param name the text to be checked
     - @return true if the text has any digit, false otherwise
     */
    public static boolean containsDigit(String name){
        for(char c: name.toCharArray()){
            if(Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

}
