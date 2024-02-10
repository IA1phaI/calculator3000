package ru.a1pha;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.List;


public class Calculator {

    // Constants
    enum Constant {
        E, PI;
    }
    private static final Map<Constant, Double> CONSTANT_VALUES = Map.ofEntries(
        entry(Constant.E, Math.E),
        entry(Constant.PI, Math.PI)
    );

    public static String getStringConstantValue(Constant constant) {
        return String.valueOf(CONSTANT_VALUES.get(constant)).replace(".", ",");
    }


    // Operators
    enum Operator {
        PLUS, MINUS, MULTIPLICATION, DIVIDE, POW, ROOT, MODULUS, LOG;
    }

    private static final Map<Operator, String> fullOperatorFormats = Map.ofEntries(
        entry(Operator.PLUS, "%s + %s = %s;\n"),
        entry(Operator.MINUS, "%s - %s = %s;\n"),
        entry(Operator.MULTIPLICATION, "%s × %s = %s;\n"),
        entry(Operator.DIVIDE, "%s ÷ %s = %s;\n"),
        entry(Operator.POW, "%s ^ %s = %s;\n"),
        entry(Operator.ROOT, "%2$s√(%1$s) = %3$s;\n"),
        entry(Operator.MODULUS, "%s mod %s = %s;\n"),
        entry(Operator.LOG, "log%2$s(%1$s) = %3$s;\n")
    );
    
    private static final Map<Operator, String> shortOperatorFormats = Map.ofEntries(
        entry(Operator.PLUS, "%s +\n"),
        entry(Operator.MINUS, "%s -\n"),
        entry(Operator.MULTIPLICATION, "%s ×\n"),
        entry(Operator.DIVIDE, "%s ÷\n"),
        entry(Operator.POW, "%s ^\n"),
        entry(Operator.ROOT, "√(%s)\n"),
        entry(Operator.MODULUS, "%s mod\n"),
        entry(Operator.LOG, "log(%s)\n")
    );


    // Functions
    enum Function {
        REVERT, FACT, LN, LG, SIN, COS, TAN, COT, ASIN, ACOS, ATAN, ACOT;
    }

    private static final Map<Function, String> functionFormats = Map.ofEntries(
        entry(Function.REVERT, "1 / %1$s = %2$s\n"),
        entry(Function.FACT, "%s! = %s;\n"),
        entry(Function.LN, "ln(%s) = %s;\n"),
        entry(Function.LG, "lg(%s) = %s;\n"),
        entry(Function.SIN, "sin(%1$s %3$s) = %2$s;\n"),
        entry(Function.COS, "cos(%1$s %3$s) = %2$s;\n"),
        entry(Function.TAN, "tan(%1$s %3$s) = %2$s;\n"),
        entry(Function.COT, "cotan(%1$s %3$s) = %2$s;\n"),
        entry(Function.ASIN, "asin(%s) = %s %s;\n"),
        entry(Function.ACOS, "acos(%s) = %s %s;\n"),
        entry(Function.ATAN, "atan(%s) = %s %s;\n"),
        entry(Function.ACOT, "acot(%s) = %s %s;\n")
    );


    // Modes for angles
    enum AngleMode {
        DEG, RAD, GRAD;
    }

    private static Map<AngleMode, Double> angleModeCoefficients = Map.ofEntries(
        entry(AngleMode.DEG, 180 / CONSTANT_VALUES.get(Constant.PI)),
        entry(AngleMode.RAD, 1.0),
        entry(AngleMode.GRAD, 200 / CONSTANT_VALUES.get(Constant.PI))
    );
    private static Map<AngleMode, String> angleModeString = Map.ofEntries(
        entry(AngleMode.DEG, "°"),
        entry(AngleMode.GRAD, "grad"),
        entry(AngleMode.RAD, "rad")
    );

    // Other Methods
    private static String reformatNumbersInString(String str) {
        str = str.replace(".", ",");
        Pattern pattern = Pattern.compile(",0\\D");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            int i = matcher.start();
            str = str.substring(0, i) + str.substring(i + 2, str.length());
            matcher = pattern.matcher(str);
        }
        return str.endsWith(",0") ? str.substring(0, str.length() - 2) : str;
    }

    // Object
    private Double firstArg = null;
    private Double secondArg = null;
    private Operator operator = null;
    private Double result = null;
    private Function function = null;
    private AngleMode angleMode = AngleMode.DEG;
    private List<String> calculatorHistory = new ArrayList<>();

    public void reset() {
        firstArg = null;
        secondArg = null;
        operator = null;
        result = null;
        function = null;
        angleMode = AngleMode.DEG;
        calculatorHistory.clear();
    }

    public void setFirstArg(double value) {
        this.firstArg = value;
        this.secondArg = null;
        this.result = null;
    }

    public void setFirstArg(String str) {
        if (str.equals(",")) {
            this.setFirstArg(0);
            return;
        }
        this.setFirstArg(Double.parseDouble(str.replace(",", ".")));
    }
    
    public void setSecondArg(double value) {
        this.secondArg = this.secondArg == null ? value : this.secondArg;
        this.result = null;
    }
    
    public void setSecondArg(String str) {
        if (str.equals(",")) {
            this.setSecondArg(0);
            return;
        }
        this.setSecondArg(Double.parseDouble(str.replace(",", ".")));
    }

    public void setConstantArg(Constant constant) {
        if (getFirstArg() == null) {
            this.setFirstArg(CONSTANT_VALUES.get(constant));
        } else {
            this.setSecondArg(CONSTANT_VALUES.get(constant));
        }
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
        this.secondArg = null;
        this.result = null;
    }

    public void setFunction(Function function) {
        this.operator = null;
        this.function = function;
    }

    public void changeAngleMode(AngleMode angleMode) {
        this.angleMode = angleMode;
    }

    public Double getFirstArg() {
        return this.firstArg;
    }

    public Double getSecondArg() {
        return this.secondArg;
    }

    public Operator getOperator() {
        return this.operator;
    }

    public Double getResult() {
        try {
            if (result == null) {
                this.solve();
            }
            return this.result;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    public void prepareNext() {
        this.firstArg = this.result;
        this.result = null;
        this.function = null;
    }

    public String getResultAsString() {
        return reformatNumbersInString(String.valueOf(this.getResult()));
    }

    public String getCalculatorHistory() {
        return this.calculatorHistory.stream().collect(Collectors.joining(""));
    }

    @Override
    public String toString() {
        try {
            String str;
            if (this.function != null) {
                str = String.format(functionFormats.get(this.function), this.firstArg, this.result, angleModeString.get(this.angleMode));
            } else if (this.result == null) {
                str = String.format(shortOperatorFormats.get(this.operator), this.firstArg, this.secondArg, this.result);
            } else {
                str = String.format(fullOperatorFormats.get(this.operator), this.firstArg, this.secondArg, this.result);
            }
            return reformatNumbersInString(str);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public void solve() {
        if (this.function != null) {
            switch (this.function) {
                case REVERT:
                    this.result = calcRevert(firstArg);
                    break;
                case FACT:
                    this.result = calcFact(firstArg);
                    break;
                case LN:
                    this.result = calcLn(firstArg);
                    break;
                case LG:
                    this.result = calcLg(firstArg);
                    break;
                    
                case SIN:
                    this.result = calcSin(firstArg, this.angleMode);
                    break;
                case COS:
                    this.result = calcCos(firstArg, this.angleMode);
                    break;
                case TAN:
                    this.result = calcTan(firstArg, this.angleMode);
                    break;
                case COT:
                    this.result = calcCot(firstArg, this.angleMode);
                    break;
                    
                case ASIN:
                    this.result = calcAsin(firstArg, this.angleMode);
                    break;
                case ACOS:
                    this.result = calcAcos(firstArg, this.angleMode);
                    break;
                case ATAN:
                    this.result = calcAtan(firstArg, this.angleMode);
                    break;
                case ACOT:
                    this.result = calcAcot(firstArg, this.angleMode);
                    break;
            }
        } else {
            switch (this.operator) {
                case PLUS:
                    this.result = calcSum(firstArg, secondArg);
                    break;
                case MINUS:
                    this.result = calcSum(firstArg, secondArg * -1);
                    break;
                case MULTIPLICATION:
                    this.result = calcProduct(firstArg, secondArg);
                    break;
                case DIVIDE:
                    this.result = calcDivision(firstArg, secondArg);
                    break;
                
                case MODULUS:
                    this.result = calcModulus(firstArg, secondArg);
                    break;
                case POW:
                    this.result = calcPow(firstArg, secondArg);
                    break;
                case ROOT:
                    this.result = calcRoot(firstArg, secondArg);
                    break;

                case LOG:
                    this.result = calcLog(firstArg, secondArg);
                    break;
            }
        }
        calculatorHistory.add(this.toString());
    }


    // Static functions
    public static double calcSum(double x, double y) {
        return x + y;
    }

    public static double calcProduct(double x, double y) {
        return x * y;
    }
    
    public static double calcDivision(double x, double y) {
        return x / y;
    }

    public static double calcRevert(double x) {
        return 1 / x;
    }

    public static double calcModulus(double x, double y) {
        return x % y;
    }

    public static double calcFact(double x) {
        int res = 1;
        for (int i = 1; i <= x; i++) {
            res *= i;
        }
        return res;
    }

    public static double calcPow(double x, double pow) {
        return Math.pow(x, pow);
    }

    public static double calcRoot(double x, double pow) {
        return calcPow(x, calcRevert(pow));
    }

    public static double calcLn(double x) {
        return Math.log(x);
    }

    public static double calcLg(double x) {
        return Math.log10(x);
    }

    public static double calcLog(double x, double base) {
        return calcLn(x) / calcLn(base);
    }

    public static double calcSin(double x, AngleMode angleMode) {
        return Math.sin(x / angleModeCoefficients.get(angleMode));
    }

    public static double calcCos(double x, AngleMode angleMode) {
        return Math.cos(x / angleModeCoefficients.get(angleMode));
    }

    public static double calcTan(double x, AngleMode angleMode) {
        return Math.tan(x / angleModeCoefficients.get(angleMode));
    }

    public static double calcCot(double x, AngleMode angleMode) {
        return 1 / calcTan(x, angleMode);
    }

    public static double calcAsin(double x, AngleMode angleMode) {
        return Math.asin(x) * angleModeCoefficients.get(angleMode);
    }

    public static double calcAcos(double x, AngleMode angleMode) {
        return Math.acos(x) * angleModeCoefficients.get(angleMode);
    }

    public static double calcAtan(double x, AngleMode angleMode) {
        return Math.atan(x) * angleModeCoefficients.get(angleMode);
    }

    public static double calcAcot(double x, AngleMode angleMode) {
        return calcAtan(1 / x, angleMode);
    }
}