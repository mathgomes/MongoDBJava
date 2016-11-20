package mongodb1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoundedRangeModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class QueryBuilderGUI {


    private JFrame j;
    private JPanel pPainelDeCima;
    private JPanel pPainelDeBaixo;
    private JComboBox jc;
    private JButton insertButton;
    private JTextArea jtAreaDeStatus;
    private JPanel panel;
    private JPanel painelInsercaoDeDados;
    private JTextField queryField;
    private JScrollBar querybar;
    private JLabel querylabel;
    private JComboBox jc2;
    private JButton go;
    private JPanel painelExibicaoDeDados;

    private QueryBuilderGUI () {
        /*Janela*/
        j = new JFrame("ICMC-USP - SCC0241 - Pratica 5");
        j.setSize(800, 600);
        j.setLayout(new BorderLayout());
        j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*Painel da parte superior (north) - com combobox e outras informações*/
        pPainelDeCima = new JPanel(new FlowLayout());
        j.add(pPainelDeCima, BorderLayout.NORTH);

        queryField = new JTextField(20);
        Font bigFont = queryField.getFont().deriveFont(Font.PLAIN, 20f);
        queryField.setFont(bigFont);
        querybar = new JScrollBar(JScrollBar.HORIZONTAL);
        BoundedRangeModel brm = queryField.getHorizontalVisibility();
        querybar.setModel(brm);
        querylabel = new JLabel("Query");
        pPainelDeCima.add(querylabel);
        pPainelDeCima.add(queryField);
        pPainelDeCima.add(querybar);

        /*Painel da parte inferior (south) - com área de status*/
        pPainelDeBaixo = new JPanel();
        j.add(pPainelDeBaixo, BorderLayout.SOUTH);
        jtAreaDeStatus = new JTextArea();
        jtAreaDeStatus.setText("Aqui é sua área de status");
        pPainelDeBaixo.add(jtAreaDeStatus);

        /*Painel tabulado na parte central (CENTER)*/
        panel = new JPanel( new FlowLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.setLayout(new GridLayout(2,5));
        j.add(panel,BorderLayout.CENTER);

        jc = new JComboBox();


        /*Tab de exibicao*/
        painelInsercaoDeDados = new JPanel();
        painelInsercaoDeDados.setBorder(BorderFactory.createLineBorder(Color.black));

        //painelInsercaoDeDados.setLayout(new BoxLayout(painelInsercaoDeDados,BoxLayout.Y_AXIS));


        JLabel l1 = new JLabel("Escolha a operação");
        JLabel l2 = new JLabel("Escolha a tabela");
        jc2 = new JComboBox();
        String[] operators = new String[14];
        operators[0] = "$eq";   operators[7] = "$nin";
        operators[1] = "$gt";   operators[8] = "$or";
        operators[2] = "$gte";   operators[9] = "$and";
        operators[3] = "$lt";   operators[10] = "$not";
        operators[4] = "$lte";   operators[11] = "$nor";
        operators[5] = "$ne";   operators[12] = "$exists";
        operators[6] = "$in";   operators[13] = "$type";
        jc = new JComboBox(operators);
        go = new JButton("Go");

        painelInsercaoDeDados.add(l2);
        painelInsercaoDeDados.add(jc2);

        painelInsercaoDeDados.add(l1);
        painelInsercaoDeDados.add(jc);

        painelInsercaoDeDados.add(jc);
        painelInsercaoDeDados.add(go);

        panel.add(painelInsercaoDeDados);


        painelExibicaoDeDados = new JPanel();
        JScrollPane jc1 = new JScrollPane(painelExibicaoDeDados);
        painelExibicaoDeDados.setBorder(BorderFactory.createLineBorder(Color.black));
        panel.add(jc1);

        j.setVisible(true);
    }

    private void DefineEventos() {
        jc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    public static void main(String[] args) {
        QueryBuilderGUI q = new QueryBuilderGUI();

    }
}
