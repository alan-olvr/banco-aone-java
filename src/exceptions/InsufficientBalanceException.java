package exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException() {
      super("[ERRO]: Saldo insuficiente para este saque. ");
    }
}
