package entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 - A single operation registered in an account: its type (deposit or
 - withdrawal), the moment it happened and the amount involved.
 */
public class Transaction {

    private final TransactionType type;
    private final LocalDateTime moment;
    private final double value;

    // Date and time format used when the transaction is displayed (e.g. 28/10/2026 14:30)
    final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /*
     - Creates a transaction.
     - @param type   the transaction type (DEPOSIT or WITHDRAW)
     - @param moment the date and time when the transaction happened
     - @param value  the amount involved
     */
    public Transaction(TransactionType type, LocalDateTime moment, double value){
        this.moment = moment;
        this.value = value;
        this.type = type;
    }

    //Returns the transaction type.
    public TransactionType getType(){
        return this.type;
    }

    // Returns the date and time when the transaction happened.
    public LocalDateTime getMoment() {
        return this.moment;
    }

    //Returns the amount involved in the transaction.
    public double getValue() {
        return this.value;
    }

    /*
     - Returns a short text for the transaction: the amount preceded by "+" for
     - deposits or "-" for withdrawals, followed by the formatted date.
     - Any other type produces a single blank space.
     - @return the formatted text of the transaction
     */
    public String formatOut() {
        if(getType().equals(TransactionType.DEPOSIT)) {
            return "R$ +" + getValue() + "\nData: " + getMoment().format(fmt);
        } else if(getType().equals(TransactionType.WITHDRAW)) {
            return "R$ -" + getValue() + "\nData: " + getMoment().format(fmt);
        } else return " ";
    }

    /*
     - Returns the full text of the transaction: type, amount and formatted date.
     - Used when the extract is printed.
     - @return the text representation of the transaction
     */
    @Override
    public String toString() {
        return "Tipo: " + getType() + "\nValor: R$ " + getValue() + "\nData: " + getMoment().format(fmt);
    }
}
