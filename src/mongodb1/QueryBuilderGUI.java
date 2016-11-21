package mongodb1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class QueryBuilderGUI {


    private JFrame j;
    private JPanel pPainelDeCima;
    private JPanel pPainelDeBaixo;
    private JComboBox<String> operatorsBox;
    private JButton queryButton;
    private JTextArea jtAreaDeStatus;
    private JPanel panel;
    private JPanel painelInsercaoDeDados;
    private JPanel painelInsercaoDeDadosAux;
    private JTextArea queryArea;
    private JScrollPane querybar;
    private JLabel querylabel;
    private JComboBox tableNamesBox;
    private JButton tableNamesButton;
    private JButton operatorButton;
    private JPanel painelExibicaoDeDados;
    private JTextArea displayArea;
    private JScrollPane displayAreascroll;
    private MongoConnection c;
    private Vector<String> operators;
    private JButton executeOperator = new JButton("Apply");
    private JComboBox<String> jc;
    private JTextArea area;
    private boolean[] index = new boolean[6];


    private QueryBuilderGUI () {
        /*Janela*/
        j = new JFrame("ICMC-USP - SCC0241 - Query Generator");
        j.setSize(800, 600);
        j.setLayout(new BorderLayout());
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*Painel da parte superior (north) - com combobox e outras informações*/
        pPainelDeCima = new JPanel(new FlowLayout());
        j.add(pPainelDeCima, BorderLayout.NORTH);

        queryArea = new JTextArea(2,15);
        Font bigFont = queryArea.getFont().deriveFont(Font.PLAIN, 20f);
        queryArea.setFont(bigFont);
        //querybar = new JScrollBar(JScrollBar.HORIZONTAL);
        //BoundedRangeModel brm = queryArea.getHorizontalVisibility();
        //querybar.setModel(brm);
        queryArea.setText("{}");
        querybar = new JScrollPane(queryArea);
        //querybar.setVerticalScrollBarPolicy(ScrollPaneConstants);
        querylabel = new JLabel("Query");

        queryButton = new JButton("Execute");

        pPainelDeCima.add(querylabel);
        //pPainelDeCima.add(queryArea);
        pPainelDeCima.add(querybar);
        pPainelDeCima.add(queryButton);

        /*Painel da parte inferior (south) - com área de status*/
        pPainelDeBaixo = new JPanel();
        j.add(pPainelDeBaixo, BorderLayout.SOUTH);
        jtAreaDeStatus = new JTextArea();
        bigFont = jtAreaDeStatus.getFont().deriveFont(Font.PLAIN, 16f);
        jtAreaDeStatus.setFont(bigFont);
        jtAreaDeStatus.setText("Selecione o nome da tabela e clique no botão de inserção");
        pPainelDeBaixo.add(jtAreaDeStatus);

        /*Painel tabulado na parte central (CENTER)*/
        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setLayout(new GridLayout(2,1));
        j.add(panel,BorderLayout.CENTER);

        operatorsBox = new JComboBox<>();


        /*Tab de exibicao*/
        painelInsercaoDeDados = new JPanel();
        painelInsercaoDeDados.setBorder(BorderFactory.createLineBorder(Color.black));

        //painelInsercaoDeDados.setLayout(new BoxLayout(painelInsercaoDeDados,BoxLayout.Y_AXIS));


        JLabel l1 = new JLabel("Escolha a operação");
        JLabel l2 = new JLabel("Escolha a tabela");
        tableNamesBox = new JComboBox();

        String[] operators2 = new String[14];
        operators = new Vector<>();
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


        painelExibicaoDeDados = new JPanel(new GridLayout());

        displayArea = new JTextArea(2,15);
        bigFont = displayArea.getFont().deriveFont(Font.PLAIN, 20f);
        displayArea.setFont(bigFont);
        //querybar = new JScrollBar(JScrollBar.HORIZONTAL);
        //BoundedRangeModel brm = queryArea.getHorizontalVisibility();
        //querybar.setModel(brm);
        displayAreascroll = new JScrollPane(displayArea);
        //querybar.setVerticalScrollBarPolicy(ScrollPaneConstants);
        //jt = new JTable(tableModel);
        //JScrollPane jc1 = new JScrollPane(jt);
        painelExibicaoDeDados.setBorder(BorderFactory.createLineBorder(Color.black));
        //painelExibicaoDeDados.add(jc1);
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

                    index[1] = true;
                }
                else if(Objects.equals(selected, "$or") || Objects.equals(selected, "$and") ||
                            Objects.equals(selected, "$nor") ) {

                    index[2] = true;
                }
                else if (Objects.equals(selected, "$not")) {

                    index[3] = true;
                }
                else if (Objects.equals(selected, "$exists")) {

                    index[4] = true;
                }
                else if (Objects.equals(selected, "$type") ) {

                    index[5] = true;
                }

            }
        });

        tableNamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String)tableNamesBox.getSelectedItem();
                //queryArea.setText("db." + selected + ".find()");
                jtAreaDeStatus.setText("Query será executada na tabela " + selected);
                //queryArea.insert("db." + selected + ".find()",queryArea.getCaretPosition());
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

                }
                else if(index[1]) {

                }
                else if(index[2]) {

                }
                else if(index[3]) {

                }
                else if(index[4]) {

                }
                else if(index[5]) {

                }
                String selected = (String)jc.getSelectedItem();
                String selectedArea = area.getText();
                String selectedOP = (String)operatorsBox.getSelectedItem();
                painelInsercaoDeDadosAux.removeAll();
                queryArea.insert(selected + " : " + "{" + selectedOP + ":" + selectedArea + " }",queryArea.getCaretPosition());
                j.setVisible(true);
            }
        });
    }


    public static void main(String[] args) {
        QueryBuilderGUI q = new QueryBuilderGUI();
        //DBObject query = (DBObject) JSON.parse("{name:{$exists:true}}");
        //c.selectFields("LE01ESTADO");


    }
}
