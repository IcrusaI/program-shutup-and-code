import java.util.HashMap;
import java.util.Map;

public class TypeChecker extends BazhenovBaseListener {
    private static final String TYPE_REAL = "!";
    private static final String TYPE_BOOLEAN = "$";
    private static final String TYPE_INTEGER = "%";

    private final Map<String, String> variables = new HashMap<>();

    // Обработка объявления переменных
    @Override
    public void exitDeclaration(BazhenovParser.DeclarationContext ctx) {
        String type = ctx.type().getText();
        ctx.IDENTIFIER().forEach(identifierContext -> {
            String varName = identifierContext.getText();

            if (variables.containsKey(varName)) {
                // Обработка ошибки: переменная уже объявлена
                System.err.println("Error: Variable " + varName + " already declared.");
            } else {
                variables.put(varName, type);
            }
        });
    }

    // Обработка присваивания переменных
    @Override
    public void exitAssignment(BazhenovParser.AssignmentContext ctx) {
        String varName = ctx.IDENTIFIER().getText();
        String declaredType = variables.get(varName);

        if (declaredType == null) {
            // Обработка ошибки: переменная не объявлена
            System.err.println("Error: Variable " + varName + " is not declared.");
        } else {
            System.out.println(varName);
            String assignedType = getType(ctx.expression());

            // Обработка ошибки: неправильное присваивание типа
            if (!(assignedType.equals(declaredType) || (assignedType.equals(TYPE_INTEGER) && TYPE_REAL.equals(declaredType)))) {
                System.err.println("Error: Type mismatch for variable " + varName + ". Expected " + declaredType + ", but got " + assignedType + ".");
            }
        }
    }

    private String getType(BazhenovParser.ExpressionContext expression) {
        // Проверяем, содержит ли выражение логические элементы
        if (expressionContainsLogicalElements(expression)) {
            return TYPE_BOOLEAN;
        }

        // Проверяем, содержит ли выражение элементы вещественного типа
        if (expressionContainsRealElements(expression)) {
            return TYPE_REAL;
        }

        // Во всех остальных случаях тип выражения - целочисленный
        return TYPE_INTEGER;
    }

    private boolean expressionContainsLogicalElements(BazhenovParser.ExpressionContext expression) {
        // Проверка на логические операции или константы
        if (!expression.relationOp().isEmpty() ||
                 expressionContains(expression, BazhenovParser.OR) ||
                expressionContains(expression, BazhenovParser.AND) ||
                expressionContains(expression, BazhenovParser.NOT) ||
                expressionContains(expression, BazhenovParser.TRUE) ||
                expressionContains(expression, BazhenovParser.FALSE)) {
            return true;
        }

        // Проверка переменных на логический тип
        return containsVariableOfType(expression, TYPE_BOOLEAN);
    }

    private boolean expressionContainsRealElements(BazhenovParser.ExpressionContext expression) {
        // Проверка на вещественные числа или операцию деления
        if (expressionContains(expression, BazhenovParser.DIV) ||
                expressionContains(expression, BazhenovParser.REAL)) {
            return true;
        }

        // Проверка переменных на тип 'real'
        return containsVariableOfType(expression, TYPE_REAL);
    }

    // Проверка наличия определенного типа токена в выражении
    private boolean expressionContains(BazhenovParser.ExpressionContext expression, int tokenType) {
        for (BazhenovParser.OperandContext operand :
                expression.operand()) {
            for (BazhenovParser.SummandContext summand :
                    operand.summand()) {

                for (BazhenovParser.MultiplicationOpContext multiplierOp :
                        summand.multiplicationOp()) {
                    if (multiplierOp.stop.getType() == tokenType) {
                        return true;
                    }
                }

                for (BazhenovParser.MultiplierContext multiplier :
                        summand.multiplier()) {
                    if (multiplier.stop.getType() == tokenType) {
                        return true;
                    }
                }
            }
        }

        return false;
//        return expression.toStringTree().contains(name);
    }

    private boolean containsVariableOfType(BazhenovParser.ExpressionContext expression, String type) {
        // Проверка наличия переменной определенного типа
        return expression.operand().stream()
                .flatMap(operand -> operand.summand().stream())
                .flatMap(summand -> summand.multiplier().stream())
                .filter(multiplier -> multiplier.IDENTIFIER() != null)
                .anyMatch(multiplier -> type.equals(variables.get(multiplier.IDENTIFIER().getText())));
    }
}