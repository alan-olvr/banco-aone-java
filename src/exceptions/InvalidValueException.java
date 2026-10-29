package exceptions;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException() {
        super("[ERRO]: Valor informado inválido. Informe um valor maior que zero ");
    }
}
