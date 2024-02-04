package ru.a1pha;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import ru.a1pha.Calculator.Operator;
import ru.a1pha.Calculator.AngleMode;
import ru.a1pha.Calculator.Constant;
import ru.a1pha.Calculator.Function;

public class MainController implements Initializable {

    @FXML
    private ImageView imageButtonBackspace;

    @FXML
    private Button button0;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private Button button7;

    @FXML
    private Button button8;

    @FXML
    private Button button9;

    @FXML
    private Button buttonChangeSign;

    @FXML
    private Button buttonClear;

    @FXML
    private Button buttonComma;

    @FXML
    private Button buttonBackspace;

    @FXML
    private Button buttonDivision;

    @FXML
    private Button buttonEquals;

    @FXML
    private Button buttonMinus;

    @FXML
    private Button buttonMod;

    @FXML
    private Button buttonMultiply;

    @FXML
    private Button buttonPlus;

    @FXML
    private Button buttonPow;

    @FXML
    private Button buttonRoot;

    @FXML
    private MenuItem menuItemAcos;

    @FXML
    private MenuItem menuItemAcotan;

    @FXML
    private MenuItem menuItemAsin;

    @FXML
    private MenuItem menuItemAtan;

    @FXML
    private MenuItem menuItemCos;

    @FXML
    private MenuItem menuItemCotan;

    @FXML
    private MenuItem menuItemE;

    @FXML
    private MenuItem menuItemFact;

    @FXML
    private MenuItem menuItemLg;

    @FXML
    private MenuItem menuItemLn;

    @FXML
    private MenuItem menuItemLog;

    @FXML
    private MenuItem menuItemPi;

    @FXML
    private MenuItem menuItemRevert;

    @FXML
    private MenuItem menuItemSin;

    @FXML
    private MenuItem menuItemTan;

    private ToggleGroup toggleGroupAngleMode = new ToggleGroup();

    @FXML
    private RadioButton radioButtonDeg;

    @FXML
    private RadioButton radioButtonGrad;

    @FXML
    private RadioButton radioButtonRad;

    @FXML
    private TextArea textAreaHistory;

    @FXML
    private TextField textFieldInput;

    @FXML
    private VBox mainRoot;

    private void clearFields() {
        try {
            if (textFieldInput.getText().isEmpty()) {
                textAreaHistory.setText("");
                radioButtonDeg.setSelected(true);
                buttonClear.setText("C");
                calculator.reset();
            } else {
                textFieldInput.setText("");
                buttonClear.setText("CE");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void deleteLast() {
        try {
            if (!textFieldInput.getText().isEmpty()) {
                textFieldInput.setText(textFieldInput.getText().substring(0, textFieldInput.getText().length() - 1));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void changeSign() {
        try {
            if (textFieldInput.getText().isEmpty()) {
                return;
            }
            if (textFieldInput.getText().charAt(0) != '-') {
                textFieldInput.setText("-" + textFieldInput.getText());
            } else {
                textFieldInput.setText(textFieldInput.getText().substring(1));
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private boolean isInputOpened = false;
    public void input(String data) {
        try {
            if (data.equals(",") && textFieldInput.getText().contains(",")) {
                isInputOpened = true;
            } else if (!isInputOpened) {
                textFieldInput.setText(data);
                isInputOpened = true;
            } else if (textFieldInput.getText().length() < 18) {
                textFieldInput.setText(textFieldInput.getText() + data);
            }
            buttonClear.setText("C");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void inputConstant(Constant constant) {
        try {
            textFieldInput.setText(Calculator.getStringConstantValue(constant));
            isInputOpened = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void setOperator(Operator operator) {
        try {
            if (textFieldInput.getText().isEmpty()) {
                return;
            }
            calculator.setFirstArg(textFieldInput.getText());
            calculator.setOperator(operator);
            textAreaHistory.setText(calculator.getCalculatorHistory() + calculator.toString());
            isInputOpened = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void changeAngleMode(AngleMode angleMode) {
        try {
            calculator.changeAngleMode(angleMode);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void solve() {
        try {
            if (calculator.getOperator() == null) {
                return;
            }
            calculator.setSecondArg(textFieldInput.getText());
            if (calculator.getResult() != null) {
                textFieldInput.setText(calculator.getResultAsString());
                textAreaHistory.setText(calculator.getCalculatorHistory());
            }
            calculator.prepareNext();
            isInputOpened = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void solveFunction(Function newFunction) {
        try {
            calculator.setFirstArg(textFieldInput.getText());
            calculator.setFunction(newFunction);
            textFieldInput.setText(calculator.getResultAsString());
            textAreaHistory.setText(calculator.getCalculatorHistory());
            calculator.prepareNext();
            isInputOpened = false;
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


    // Keyboard
    final KeyCombination KEY_CLEAR = new KeyCodeCombination(KeyCode.ESCAPE);
    final KeyCombination KEY_BACKSPACE = new KeyCodeCombination(KeyCode.BACK_SPACE);
    final KeyCombination KEY_CHANGE_SIGN = new KeyCodeCombination(KeyCode.SLASH, KeyCodeCombination.SHIFT_DOWN);
    final KeyCombination KEY_PLUS = new KeyCodeCombination(KeyCode.EQUALS, KeyCodeCombination.SHIFT_DOWN);
    final KeyCombination KEY_MINUS = new KeyCodeCombination(KeyCode.MINUS, KeyCodeCombination.SHORTCUT_ANY);
    final KeyCombination KEY_MULTIPLICATION = new KeyCodeCombination(KeyCode.DIGIT8, KeyCodeCombination.SHIFT_DOWN);
    final KeyCombination KEY_DIVIDE = new KeyCodeCombination(KeyCode.SLASH);
    final KeyCombination KEY_MODULUS = new KeyCodeCombination(KeyCode.M, KeyCodeCombination.SHIFT_DOWN);
    final KeyCombination KEY_POW = new KeyCodeCombination(KeyCode.DIGIT6, KeyCodeCombination.SHIFT_DOWN);
    final KeyCombination KEY_ROOT = new KeyCodeCombination(KeyCode.SLASH, KeyCodeCombination.ALT_DOWN);
    final KeyCombination KEY_EQUALS = new KeyCodeCombination(KeyCode.EQUALS);
    final KeyCombination KEY_0 = new KeyCodeCombination(KeyCode.DIGIT0);
    final KeyCombination KEY_1 = new KeyCodeCombination(KeyCode.DIGIT1);
    final KeyCombination KEY_2 = new KeyCodeCombination(KeyCode.DIGIT2);
    final KeyCombination KEY_3 = new KeyCodeCombination(KeyCode.DIGIT3);
    final KeyCombination KEY_4 = new KeyCodeCombination(KeyCode.DIGIT4);
    final KeyCombination KEY_5 = new KeyCodeCombination(KeyCode.DIGIT5);
    final KeyCombination KEY_6 = new KeyCodeCombination(KeyCode.DIGIT6);
    final KeyCombination KEY_7 = new KeyCodeCombination(KeyCode.DIGIT7);
    final KeyCombination KEY_8 = new KeyCodeCombination(KeyCode.DIGIT8);
    final KeyCombination KEY_9 = new KeyCodeCombination(KeyCode.DIGIT9);
    final KeyCombination KEY_COMMA = new KeyCodeCombination(KeyCode.COMMA);
    final KeyCombination KEY_PERIOD = new KeyCodeCombination(KeyCode.PERIOD);

    /**
     * last condition of text in button CLEAR
     */
    private String buttonClearBufferedLabel = null;
    @FXML
    void handleOnKeyPressed(KeyEvent event) {
        mainRoot.requestFocus();
        if (event.getCode() == KeyCode.CONTROL) {
            buttonChangeSign.setText(KEY_CHANGE_SIGN.getDisplayText());
            buttonPlus.setText(KEY_PLUS.getDisplayText());
            buttonMultiply.setText(KEY_MULTIPLICATION.getDisplayText());
            buttonPow.setText(KEY_POW.getDisplayText());
            buttonDivision.setText(KEY_DIVIDE.getDisplayText());
            buttonMod.setText(KEY_MODULUS.getDisplayText());
            buttonRoot.setText(KEY_ROOT.getDisplayText());
            buttonClearBufferedLabel = buttonClear.getText();
            buttonClear.setText(KEY_CLEAR.getDisplayText());
            buttonBackspace.setText("←");
            imageButtonBackspace.setVisible(false);
        }
        if (KEY_CLEAR.match(event)) {
            buttonClear.fire();
        }
        if (KEY_BACKSPACE.match(event)) {
            buttonBackspace.fire();
        }
        if (KEY_CHANGE_SIGN.match(event)) {
            this.changeSign();
        }
        if (KEY_PLUS.match(event)) {
            this.setOperator(Operator.PLUS);
        }
        if (KEY_MINUS.match(event)) {
            this.setOperator(Operator.MINUS);
        }
        if (KEY_MULTIPLICATION.match(event)) {
            this.setOperator(Operator.MULTIPLICATION);
        }
        if (KEY_DIVIDE.match(event)) {
            this.setOperator(Operator.DIVIDE);
        }
        if (KEY_MODULUS.match(event)) {
            this.setOperator(Operator.MODULUS);
        }
        if (KEY_POW.match(event)) {
            this.setOperator(Operator.POW);
        }
        if (KEY_ROOT.match(event)) {
            this.setOperator(Operator.ROOT);
        }
        if (KEY_EQUALS.match(event) || event.getCode() == KeyCode.ENTER) {
            this.solve();
        }

        if (KEY_0.match(event)) {
            input("0");
        }
        if (KEY_1.match(event)) {
            input("1");
        }
        if (KEY_2.match(event)) {
            input("2");
        }
        if (KEY_3.match(event)) {
            input("3");
        }
        if (KEY_4.match(event)) {
            input("4");
        }
        if (KEY_5.match(event)) {
            input("5");
        }
        if (KEY_6.match(event)) {
            input("6");
        }
        if (KEY_7.match(event)) {
            input("7");
        }
        if (KEY_8.match(event)) {
            input("8");
        }
        if (KEY_9.match(event)) {
            input("9");
        }
        if (KEY_COMMA.match(event) || KEY_PERIOD.match(event)) {
            input(",");
        }
    }

    @FXML
    void handleOnKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            buttonChangeSign.setText("+/-");
            buttonPlus.setText("+");
            buttonMultiply.setText("×");
            buttonDivision.setText("÷");
            buttonPow.setText("xʸ");
            buttonMod.setText("mod");
            buttonRoot.setText("ʸ√x");
            buttonClear.setText(buttonClearBufferedLabel);
            buttonBackspace.setText(null);
            imageButtonBackspace.setVisible(true);;
        }
    }


    // INIT
    private final Calculator calculator = new Calculator();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainRoot.requestFocus();
        // Input Numbers
        List<Button> listNumButtons = List.of(button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonComma);
        for (Button button : listNumButtons) {
            button.setOnAction(event -> {
                input(button.getText());
            });
        }

        // Controls
        buttonChangeSign.setOnAction(event -> {
            changeSign();
        });
        buttonBackspace.setOnAction(event -> {
            deleteLast();
        });
        buttonClear.setOnAction(event -> {
            clearFields();
        });
        // Menu Constants
        menuItemE.setOnAction(event -> {
            inputConstant(Constant.PI);
        });
        menuItemPi.setOnAction(event -> {
            inputConstant(Constant.PI);
        });

        // Simple operations
        buttonPlus.setOnAction(event -> {
            setOperator(Operator.PLUS);
        });
        buttonMinus.setOnAction(event -> {
            setOperator(Operator.MINUS);
        });
        buttonMultiply.setOnAction(event -> {
            setOperator(Operator.MULTIPLICATION);
        });
        buttonDivision.setOnAction(event -> {
            setOperator(Operator.DIVIDE);
        });
        buttonMod.setOnAction(event -> {
            setOperator(Operator.MODULUS);
        });
        buttonPow.setOnAction(event -> {
            setOperator(Operator.POW);
        });
        buttonRoot.setOnAction(event -> {
            setOperator(Operator.ROOT);
        });
        buttonEquals.setOnAction(event -> {
            solve();
        });
        
        // Menu Functions
        menuItemFact.setOnAction(event -> {
            solveFunction(Function.FACT);
        });
        menuItemRevert.setOnAction(event -> {
            solveFunction(Function.REVERT);
        });
        menuItemLn.setOnAction(event -> {
            solveFunction(Function.LN);
        });
        menuItemLg.setOnAction(event -> {
            solveFunction(Function.LG);
        });
        menuItemLog.setOnAction(event -> {
            setOperator(Operator.LOG);
        });
        menuItemSin.setOnAction(event -> {
            solveFunction(Function.SIN);
        });

        // Menu Trigonometry
        menuItemCos.setOnAction(event -> {
            solveFunction(Function.COS);
        });
        menuItemTan.setOnAction(event -> {
            solveFunction(Function.TAN);
        });
        menuItemCotan.setOnAction(event -> {
            solveFunction(Function.COT);
        });
        menuItemAsin.setOnAction(event -> {
            solveFunction(Function.ASIN);
        });
        menuItemAcos.setOnAction(event -> {
            solveFunction(Function.ACOS);
        });
        menuItemAtan.setOnAction(event -> {
            solveFunction(Function.ATAN);
        });
        menuItemAcotan.setOnAction(event -> {
            solveFunction(Function.ACOT);

        });

        // Angle mode buttons
        radioButtonDeg.setToggleGroup(toggleGroupAngleMode);
        radioButtonDeg.setOnAction(event -> {
            changeAngleMode(AngleMode.DEG);
        });

        radioButtonGrad.setToggleGroup(toggleGroupAngleMode);
        radioButtonGrad.setOnAction(event -> {
            changeAngleMode(AngleMode.GRAD);
        });

        radioButtonRad.setToggleGroup(toggleGroupAngleMode);
        radioButtonRad.setOnAction(event -> {
            changeAngleMode(AngleMode.RAD);
        });
    }
}
