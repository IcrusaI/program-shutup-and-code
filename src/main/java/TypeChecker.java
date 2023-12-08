import java.util.HashMap;
import java.util.Map;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class TypeChecker extends BazhenovBaseListener {

    private Map<String, String> variables = new HashMap<>();

    // Переопределяем метод для обработки объявления переменных
    @Override
    public void exitVarDeclaration(BazhenovParser.VarDeclarationContext ctx) {
        String type = ctx.type().getText();
        for (BazhenovParser.IdentifierContext identifier : ctx.identifier()) {
            String varName = identifier.getText();

            if (variables.get(varName) != null) {
                System.err.println("Error: Variable " + varName + " already declared.");
            }

            // Записываем переменную в таблицу переменных
            variables.put(varName, type);
        }
    }

    // Переопределяем метод для обработки присваивания переменных
    @Override
    public void exitAssignment(BazhenovParser.AssignmentContext ctx) {
        String varName = ctx.identifier().getText();
        String assignedType = getType(ctx.expression());
        String declaredType = variables.get(varName);

        if (declaredType == null) {
            // Обработка ошибки: переменная не объявлена
            System.err.println("Error: Variable " + varName + " is not declared.");
        } else if (!assignedType.equals(declaredType)) {
            // Обработка ошибки: неправильное присваивание типа
            System.err.println("Error: Type mismatch for variable " + varName + ". Expected " + declaredType + ", but got " + assignedType + ".");
        }
    }

    // Дополнительный метод для получения типа выражения
    private String getType(BazhenovParser.ExpressionContext expression) {
        for (BazhenovParser.OperandContext operand : expression.operand()) {
            for (BazhenovParser.AddendContext addend : operand.addend()) {
                for (BazhenovParser.MultiplierContext multiplier : addend.multiplier()) {
                    if (multiplier.number() != null) {
                        if (multiplier.number().INTEGER() != null) {
                            return "%";
                        } else if (multiplier.number().REAL() != null) {
                            return "!";
                        }
                    } else if (multiplier.logicalConstant() != null) {
                        return "$";
                    }
                }
            }
        }
        return "UNKNOWN";
    }
}