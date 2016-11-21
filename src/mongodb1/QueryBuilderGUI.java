package mongodb1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class QueryBuilderGUI {


    private JFrame j;
    private JComboBox<String> operatorsBox;
    private JButton queryButton;
    private JTextArea jtAreaDeStatus;
    private JPanel painelInsercaoDeDadosAux;
    private JTextArea queryArea;
    private JComboBox tableNamesBox;
    private JButton tableNamesButton;
    private JButton operatorButton;
    private JTextArea displayArea;
    private MongoConnection c;

    private JButton executeOperator = new JButton("Apply");
    private JComboBox<String> jc;
    private JTextArea area;
    private Vector<JTextArea> areaVec = new Vector<>();
    private JTextArea argCounter = new JTextArea();
    private boolean[] index = new boolean[6];
    private JButton executeOperatorAux = new JButton("Choose");
    private JComboBox<String> jcbExists = new JComboBox<>();


    private QueryBuilderGUI () {
        /*Janela*/
        j = new JFrame("ICMC-USP - SCC0241 - Query Generator");
        j.setSize(800, 600);
        j.setLayout(new BorderLayout());
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*Painel da parte superior (north) - com combobox e outras informações*/
        JPanel pPainelDeCima = new JPanel(new FlowLayout());
        j.add(pPainelDeCima, BorderLayout.NORTH);

        queryArea = new JTextArea(2,15);
        Font bigFont = queryArea.getFont().deriveFont(Font.PLAIN, 20f);
        queryArea.setFont(bigFont);
        queryArea.setText("{}");
        JScrollPane querybar = new JScrollPane(queryArea);
        JLabel querylabel = new JLabel("Query");

        queryButton = new JButton("Execute");

        pPainelDeCima.add(querylabel);
        //pPainelDeCima.add(queryArea);
        pPainelDeCima.add(querybar);
        pPainelDeCima.add(queryButton);

        /*Painel da parte inferior (south) - com área de status*/
        JPanel pPainelDeBaixo = new JPanel();
        j.add(pPainelDeBaixo, BorderLayout.SOUTH);
        jtAreaDeStatus = new JTextArea();
        bigFont = jtAreaDeStatus.getFont().deriveFont(Font.PLAIN, 16f);
        jtAreaDeStatus.setFont(bigFont);
        jtAreaDeStatus.setText("Selecione o nome da tabela e clique no botão de inserção");
        pPainelDeBaixo.add(jtAreaDeStatus);

        /*Painel tabulado na parte central (CENTER)*/
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setLayout(new GridLayout(2,1));
        j.add(panel,BorderLayout.CENTER);

        operatorsBox = new JComboBox<>();


        /*Tab de exibicao*/
        JPanel painelInsercaoDeDados = new JPanel();
        painelInsercaoDeDados.setBorder(BorderFactory.createLineBorder(Color.black));

        //painelInsercaoDeDados.setLayout(new BoxLayout(painelInsercaoDeDados,BoxLayout.Y_AXIS));


        JLabel l1 = new JLabel("Escolha a operação");
        JLabel l2 = new JLabel("Escolha a tabela");
        tableNamesBox = new JComboBox();

        String[] operators2 = new String[14];
        Vector<String> operators = new Vector<>();
        operators2[6] = "$nin";  operators2[13] = "$eq";
        operators2[0] = "$gt";   operators2[7] = "$or";
        operators2[1] = "$gte";   operators2[8] = "$and";
        operators2[2] = "$lt";   operators2[9] = "$not";
        operators2[3] = "$lte";   operators2[10] = "$nor";
        operators2[4] = "$ne";   operators2[11] = "$exists";
        operators2[5] = "$in";   operators2[12] = "$type";
        operators.addAll(Arrays.asList(operators2));

        operatorsBox = new JComboBox<>(operators2);
        tableNamesButton = new JButton("add Collection");
        operatorButton = new JButton("add Operator");

        painelInsercaoDeDados.add(l2);
        painelInsercaoDeDados.add(tableNamesBox);
        painelInsercaoDeDados.add(tableNamesButton);

        painelInsercaoDeDados.add(l1);
        painelInsercaoDeDados.add(operatorsBox);
        painelInsercaoDeDados.add(operatorButton);

        painelInsercaoDeDadosAux = new JPanel();
        painelInsercaoDeDadosAux.setBorder(BorderFactory.createLineBorder(Color.black));
        painelInsercaoDeDados.add(painelInsercaoDeDadosAux);


        panel.add(painelInsercaoDeDados);


        JPanel painelExibicaoDeDados = new JPanel(new GridLayout());

        displayArea = new JTextArea(2,15);
        bigFont = displayArea.getFont().deriveFont(Font.PLAIN, 20f);
        displayArea.setFont(bigFont);
        JScrollPane displayAreascroll = new JScrollPane(displayArea);
        painelExibicaoDeDados.setBorder(BorderFactory.createLineBorder(Color.black));
        painelExibicaoDeDados.add(displayAreascroll);
        panel.add(painelExibicaoDeDados);





        j.setVisible(true);

        c = new MongoConnection();
        if( c.connect() ) {
            c.displayCollectionNames(tableNamesBox);
        }
        this.DefineEventos();
    }

    private void DefineEventos() {

        operatorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Arrays.fill(index,false);
                Vector<String> fields = c.selectFields((String) tableNamesBox.getSelectedItem());
                String selected = (String)operatorsBox.getSelectedItem();

                if(Objects.equals(selected, "$gt") || Objects.equals(selected, "$gte") ||
                        Objects.equals(selected,"$lt") || Objects.equals(selected, "$lte")
                        || Objects.equals(selected, "$ne") ||Objects.equals(selected, "$eq") ) {

                    jc = new JComboBox<>(fields);
                    JLabel l = new JLabel("Escolha o atributo");
                    painelInsercaoDeDadosAux.add(l);
                    painelInsercaoDeDadosAux.add(jc);

                    JLabel l2 = new JLabel("Digite o valor");
                    area = new JTextArea(1,5);

                    painelInsercaoDeDadosAux.add(l2);
                    painelInsercaoDeDadosAux.add(area);

                    painelInsercaoDeDadosAux.add(executeOperator);
                    j.setVisible(true);

                    index[0] = true;

                }
                else if(Objects.equals(selected, "$in") || Objects.equals(selected, "$nin") ) {

                    jc = new JComboBox<>(fields);
                    JLabel l = new JLabel("Escolha o atributo");
                    painelInsercaoDeDadosAux.add(l);
                    painelInsercaoDeDadosAux.add(jc);

                    JLabel l2 = new JLabel("Escolha quantos valores");
                    argCounter = new JTextArea(1,3);
                    painelInsercaoDeDadosAux.add(l2);
                    painelInsercaoDeDadosAux.add(argCounter);

                    painelInsercaoDeDadosAux.add(executeOperatorAux);
                    j.setVisible(true);
                    index[1] = true;
                }
                else if(Objects.equals(selected, "$or") || Objects.equals(selected, "$and") ||
                            Objects.equals(selected, "$nor") ) {

                    JLabel l2 = new JLabel("Escolha quantas condições");
                    argCounter = new JTextArea(1,3);
                    painelInsercaoDeDadosAux.add(l2);
                    painelInsercaoDeDadosAux.add(argCounter);
                    painelInsercaoDeDadosAux.add(executeOperator);
                    j.setVisible(true);
                    index[2] = true;
                }
                else if (Objects.equals(selected, "$not")) {
                    // not implemented
                    index[3] = true;
                }
                else if (Objects.equals(selected, "$exists")) {
                    jcbExists.removeAll();
                    jc = new JComboBox<>(fields);
                    JLabel l = new JLabel("Escolha o atributo");
                    painelInsercaoDeDadosAux.add(l);
                    painelInsercaoDeDadosAux.add(jc);

                    JLabel l2 = new JLabel("Escolha o valor");
                    jcbExists.addItem("true");
                    jcbExists.addItem("false");

                    painelInsercaoDeDadosAux.add(l2);
                    painelInsercaoDeDadosAux.add(jcbExists);

                    painelInsercaoDeDadosAux.add(executeOperator);
                    j.setVisible(true);

                    index[4] = true;
                }
                else if (Objects.equals(selected, "$type") ) {
                    jcbExists.removeAll();
                    jc = new JComboBox<>(fields);
                    JLabel l = new JLabel("Escolha o atributo");
                    painelInsercaoDeDadosAux.add(l);
                    painelInsercaoDeDadosAux.add(jc);

                    JLabel l2 = new JLabel("Escolha o tipo");
                    jcbExists.addItem("double");    jcbExists.addItem("null");
                    jcbExists.addItem("string");    jcbExists.addItem("regex");
                    jcbExists.addItem("object");    jcbExists.addItem("int");
                    jcbExists.addItem("array");     jcbExists.addItem("long");
                    jcbExists.addItem("binData");   jcbExists.addItem("minKey");
                    jcbExists.addItem("objectId");  jcbExists.addItem("maxKey");
                    jcbExists.addItem("bool");      jcbExists.addItem("timestamp");
                    jcbExists.addItem("date");

                    painelInsercaoDeDadosAux.add(l2);
                    painelInsercaoDeDadosAux.add(jcbExists);

                    painelInsercaoDeDadosAux.add(executeOperator);
                    j.setVisible(true);

                    index[5] = true;
                }

            }
        });

        tableNamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)tableNamesBox.getSelectedItem();
                jtAreaDeStatus.setText("Query será executada na tabela " + selected);
            }
        });
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = (String)tableNamesBox.getSelectedItem();
                String query = queryArea.getText();
                c.executeQuery(query,tableName,displayArea);
                System.out.println(query + '\n' + tableName);
                jtAreaDeStatus.setText("Query db." + tableName + ".find(" + query + ") executada");
            }
        });

        executeOperator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(index[0]) {
                    String selected = (String)jc.getSelectedItem();
                    String selectedArea = area.getText();
                    String selectedOP = (String)operatorsBox.getSelectedItem();
                    painelInsercaoDeDadosAux.removeAll();
                    queryArea.insert(selected + " : " + "{ " + selectedOP + ":" + selectedArea + " }",queryArea.getCaretPosition());
                }
                else if(index[1]) {
                    String selected = (String)jc.getSelectedItem();
                    String selectedOP = (String)operatorsBox.getSelectedItem();
                    Vector<String> s1 = new Vector<>();
                    for( JTextArea s : areaVec) {
                        s1.add(s.getText());
                    }
                    painelInsercaoDeDadosAux.removeAll();

                    queryArea.insert(selected + " : " + "{ " + selectedOP + ":" +" [" + s1.firstElement(),queryArea.getCaretPosition());
                    s1.remove(0);
                    for(String s : s1) {
                        queryArea.insert("," + s,queryArea.getCaretPosition());
                    }
                    queryArea.insert("]" + " }",queryArea.getCaretPosition());
                }
                else if(index[2]) {
                    int counter = Integer.parseInt(argCounter.getText());
                    String selectedOP = (String)operatorsBox.getSelectedItem();

                    painelInsercaoDeDadosAux.removeAll();

                    queryArea.insert(selectedOP + ":" + "[ { }",queryArea.getCaretPosition());
                    for( int i =0; i < counter-1; i ++) {
                        queryArea.insert(",{ }",queryArea.getCaretPosition());
                    }
                    queryArea.insert("] ",queryArea.getCaretPosition());
                }
                else if(index[3]) {
                    // not implemented
                }
                else if(index[4]) {
                    String selected = (String)jc.getSelectedItem();
                    String selectedOP = (String)operatorsBox.getSelectedItem();
                    String option = (String)jcbExists.getSelectedItem();
                    painelInsercaoDeDadosAux.removeAll();
                    queryArea.insert(selected + " : " + "{ " + selectedOP + ":" + option + " }",queryArea.getCaretPosition());

                }
                else if(index[5]) {
                    String selected = (String)jc.getSelectedItem();
                    String selectedOP = (String)operatorsBox.getSelectedItem();
                    String option = (String)jcbExists.getSelectedItem();
                    painelInsercaoDeDadosAux.removeAll();
                    queryArea.insert(selected + " : " + "{ " + selectedOP + ":" + "\"" + option + "\"" + " }",queryArea.getCaretPosition());
                }

                j.setVisible(true);
            }
        });

        executeOperatorAux.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int counter = Integer.parseInt(argCounter.getText());
                JLabel l3 = new JLabel("Escolha quais valores");
                painelInsercaoDeDadosAux.add(l3);

                for( int i =0; i < counter; i ++) {
                    areaVec.add(new JTextArea(1,5));
                    painelInsercaoDeDadosAux.add(areaVec.elementAt((i)));
                }
                painelInsercaoDeDadosAux.add(executeOperator);
                j.setVisible(true);
            }
        });
    }


    public static void main(String[] args) {
        QueryBuilderGUI q = new QueryBuilderGUI();

    }
}
