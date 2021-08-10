package mngr;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * Information window
 * @author MrUnknown404
 */
public class HelpFrame extends javax.swing.JFrame {
    /**
     * Creating the information frame
     * @param visualVar int oprion for needed case of information
     * where 0 - main, 1 - menu, 2 - orders, 3 - errors, 4 - autofill 
     */
    public HelpFrame(int visualVar) {
        initComponents();
        myInit();
        infoTextSetUp(visualVar);
        
        ImageIcon icon = new ImageIcon("src//help.png");
        setIconImage(icon.getImage());
        setResizable(false);
    }
    
    private JTextArea textField;
    private JScrollPane helpScrollPane;
    
    /**
     * Initial method
     */
    private void myInit(){
        textField = new JTextArea();
        textField.setEditable(false);
        textField.setColumns(45);
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        textField.setVisible(true);
        textField.setOpaque(false);
        textField.setAutoscrolls(false);
        textField.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        textField.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
        
        helpScrollPane = new JScrollPane(textField);
        helpScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        helpScrollPane.setSize(helpPanel.getSize());
        helpScrollPane.setEnabled(true);
        helpScrollPane.setVisible(true);
        
        DefaultCaret caret = (DefaultCaret) textField.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        helpPanel.add(helpScrollPane);
        pack();
    }
    /**
     * Setter of the text in frame
     * @param visualVar an int option of needed information
     */
    private void infoTextSetUp(int visualVar) {
        String defaultVar = "        Менеджер состоит из панели меню, панель заказов и панели инструментов (где находятся кнопки и информационное окно).\n\n"
                + "        На панели заказов имеется две вкладки: \"Текущие проекты\" и \"Архив\". Первая содержит выкладку по заказам в работе, активным; вторая - "
                + "по завершенным, выполненным. На обеих таблицах информация по заказам может быть отсортирована по любому столбцу, если "
                + "дважды нажать на соответствующий заголовок (по умолчанию сортировка стоит по сроку сдачи, от ближайшего). Дважды нажав на поле "
                + "\"Описание\", откроется окно для просмотра\\изменения описания по выбранному заказу, а нажав на кнопку в последнем столбце - проводник "
                + "с директорией этого же заказа, где хранятся все данные по нему (которые вы добавите, конечно).\n\n"
                + "        На панели инструментов имеется 4 кнопки, позволяющие добавлять-редактировать-удалять данные по заказам (НО: редактирование заказов из архива запрещено! "
                + "Для полей архива возможен только просмотр\\изменение описание по двойному нажатию соответствующему полю), а также обновлять базу (она же таблица, поскольку они взаимосвязаны). "
                + "Также имеется информационное окно, которое выводит сегодняшнюю дату для удобства и определённую сводку по актуальной базе.\n\n"
                + "        Меню состоит из категорий \"Файл\", где можно восстановить системные данные или же обновить (сопоставить и сделать актуальными) "
                + "системные файлы для корректной работы программы; \"Функции\", позволяет изменить (указать) путь к месту хранения всех данных и "
                + "обновить резерв данных; \"Справка\" - без комментариев.";
                + "обновить резерв данных; \"Справка\" - без комментариев.\n\n"
                + "        В таблице текущих заказов записи будут окрашиватся в несколько цветов: жёлтый- когда до сдачи заказа осталось 6 дней, красный- "
                + "осталось 3 дня, серым - при задержке сдачи до 5 дней, оранжевым- больше 5 дней. В архив переносятся заказы, окрашенные в оранжевый цвет, "
                + "при нажатии на кнопку \"Обновить базу\". Из архива восстанавливать заказы не допускается, возможно только добавление и удаление.\n\n\n"
                + "                                                   Спроектировано и реализовано by Subaev RN.";
                + "обновить резерв данных; \"Справка\" - без комментариев.\n\n"
                + "        В таблице текущих заказов записи будут окрашиватся в несколько цветов: жёлтый- когда до сдачи заказа осталось 6 дней, красный- \n"
                + "осталось 3 дня, серым - при задержке сдачи до 5 дней, оранжевым- больше 5 дней. В архив переносятся заказы, окрашенные в оранжевый цвет, \n"
                + "при нажатии на кнопку \"Обновить базу\". Из архива восстанавливать заказы не допускается, возможно только добавление и удаление.";
        
        String menuVar = "       В разделе \"Файл\" есть функция \"Восстановить системные файлы\", которая нужна для того, чтобы из резерва скопировать и перенести их "
                + "в директорию, с которой работает программа. Это нужно в тех случаях, когда всплывает соответствующая ошибка об их отсутствии или при некорректном ручном "
                + "переносе.\n"
                + "\"Обновить данные по заказам\" позволяет пробежаться по текущим заказам как активным, так и в архиве, и соотнести их с записями в системных "
                + "файлах. Таким образом они обновляются: старые записи удаляются, новые проверяются и остаются неизменными. Это нужно использовать, если вы выбрали "
                + "новую корневую дирректорию (изменили корневой путь) или если вручную были удалены какие-либо папки из директорий хранения файлов заказов.\n\n"
                + "       В разделе \"Функции\" имеется возможность изменить корневой путь: указать новый путь к месту хранения всех заказов (данных по заказам). "
                + "При выборе производится проверка на наличие всех нужных поддиректорий и системных файлов, то есть не нужно заранее вручную создавать всё, что нужно для "
                + "корректного функционирования программы, с одним условием: если по предыдущему пути не было каких-либо системных файлов, то они будут созданы пустыми. Это "
                + "означает, что при наличии по старому пути файлов, их контент будет перенесён, в противном- определённые поля заказов из новой директории "
                + "будут пустыми и их нужно будет заполнить в программе.\n"
                + "Также, есть функция \"Дамп информации\", где можно либо его обновить, либо его очистить. Обновление "
                + "переносит всю информацию и файлы по каждому заказу и системные файлы в директорию резерва, а очистка, соответственно, полностью резерв удаляет. Рекомендация: "
                + "периодически проводите обновление дампа, чтобы всегда имелась запасная дирректория.\n\n"
                + "       В разделе \"Справка\" можно получить справочную информацию по всем ключевым и общим аспектам работы менеджера.";
        
        String ordersWorkVar = "       В указанной корневой директории имеются такие папки, как: Текущие заказы, АРХИВ и config. В текущих заказах хранятся папки с данными по соответствующим "
                + "заказам, в Архиве работают те же правила, а директория config используется программой и содержит системные файлы и директорию резерва - dump.\n\n"
                + "       В корневой директории программа хранит информацию по каждому заказу, также как и позволяет хранить файлы, которые вы туда положите по каждому из "
                + "заказов. Итак, менеджер работает с комбинацией срок_сдачи + заказчик, позволяя однозначно идентифицировать каждый объект. Эта же комбинация и является "
                + "обязательным форматом именования любых директорий по всем заказам, в противном случае (если вы вручную перенесли какую-то папку в соответствующую "
                + "директорию, но не переименовали её\\неправильно назвали) такие папки будут игнорироваться.\n\n"
                + "       [!!!] При ручном добавлении новых папок в соответствующие директории необходимо следовать следующему шаблону именования, чтобы они были "
                + "программно считаны: \"ГГГГ.ММ.ДД.{ПРОБЕЛ}{ЗАКАЗЧИК]\", где заказчик не должен быть пустым и обязателен пробел между датой и заказчиком. В имени заказчиков"
                + "запрещается использовать специальные символы, т.к. по ним создаются директории для хранения, правила такие же, как и при именовании обычных папок в проводнике "
                + "компьютера. Дата в данном шаблоне - это дата сдачи заказа.\n\n"
                + "       При добавлении нового заказа происходит создание новой папки, привязка к ней соответствующей кнопки, обновление системной информации менеджера и, "
                + "собственно, его вывод в таблицу. При редактирование полей таблицы происходит поиск по их ключу и, опять же, обновление информации. При удалении производится "
                + "очистка папки заказа, полностью, удаление её данных из системы и сброс записи в таблице.";
        
        String errorsWorkVar = "       В процессе работы с программой следует руководствоваться справочной информацией, но даже так могут возникать определённые проблемы. "
                + "Здесь описаны существующие обработчики ошибок и способы разрешения каких-либо проблемных ситуаций.\n\n"
                + "       При удалении заказов, очистке дампа, восстановлении системных файлов будут всплывать предупреждающие сообщения о подтверждении действия. "
                + "Надо понимать, что очистив дамп и восстановив системные данные, будет потеряна вся информация об описании и даты принятия всех заказов, поэтому "
                + "восстанавливать данные стоит лишь после обновления дампа (если это не ситуация, при которой были случайно удалены системные файлы и дамп пуст, тогда "
                + "восстановление вернёт пустые файлы конфигураций, что уже хорошо).\n\n"
                + "       При появлении ошибки о неналичии системных файлов следует либо указать новый корневой путь, либо восстановить их. Здесь нужно выбирать, исходя из ситуации: "
                + "если при выборе нового пути указать текущий, то данные тоже будут восстановлены, но пустыми, а если у вас имеется дамп, то стоит выполнить восстановление. "
                + "Также, возможна ситуация, при которой нужно перенести хранилище в другое место на диске, тогда при запуске менеджера эта ошибка тоже всплывёт, но её единственно "
                + "верным решением будет указание нового пути к данному месту на диске.\n\n"
                + "       При возникновении какой-либо сторонней проблемы, влияющей на корректной работы программы, следует сначала выбрать новый корневой путь, указав там текущую директорию, "
                + "       При появлении ошибки о неналичии системных файлов следует либо указать новый корневой путь, либо восстановить их. Здесь нужно выбирать, исходя из ситуации: "
                + "если при выборе нового пути указать текущий, то данные тоже будут восстановлены, но пустыми, а если у вас имеется дамп, то стоит выполнить восстановление. "
                + "Также, возможна ситуация, при которой нужно перенести хранилище в другое место на диске, тогда при запуске менеджера эта ошибка тоже всплывёт в том случае, "
                + "если раньше корневой путь не указывался приложению, но единственно верное решение в данном случае - указание нового пути к новому месту на диске.\n\n"
                + "верное решение - указание нового пути к данному месту на диске.\n\n"
                + "       При возникновении какой-либо сторонней проблемы, влияющей на корректную работу программы, следует сначала выбрать новый корневой путь, указав там текущую директорию, "
                + "после выполнить восстановление и обновление данных по заказам. Проблема должна быть решена.";
        
        String autofillVar = "       При заполнении полей заказов обязательно заполнять все поля, кроме описания. Но при всём этом, поле с датой сдачи автоматически заполняется текущей датой "
                + "с возможностью выбора последнего разряда года, поле срока сдачи заполняется той же маской года, а оставшиеся цифры нулями, а если оставить поле Заказчика пустым, то "
                + "оно будет программно заполнено как \"None\".\n\n"
                + "       При считывание программой новых заказов (новых папок, которых не было ранее), посколько мы имеем лишь ключ из срока сдачи и заказчика, дата поступления "
                + "будет определена как текущая дата, а описание останется пустым. Это можно будет отредактировать позже.\n\n"
                + "       Следует напомнить, что нужно соблюдать обязательные форматы имени для всех данных, с которыми работает менеджер: директория для действующих заказов - "
                + "\"Текущие заказы\", для архива - \"АРХИВ\", для системных файлов - \"config\", для дампа - \"dump\"; для папок заказов - \"ГГГГ.ММ.ДД.{ПРОБЕЛ}{ЗАКАЗЧИК]\", "
                + "где пробел обязателен, ровно как и наименование заказчика должно быть не пустым и не начинаться с дублирующего пробела!";
        
        switch (visualVar) {
            case 0 -> {
                textField.setText(defaultVar);
                helpLabel.setText("Общая справка");
            }
            case 1 -> {
                textField.setText(menuVar);
                helpLabel.setText("Информания о панеле меню");
            }
            case 2 -> {
                textField.setText(ordersWorkVar);
                helpLabel.setText("Справка по заказам и их файлам");
            }
            case 3 -> {
                textField.setText(errorsWorkVar);
                helpLabel.setText("Возможные ошибки и способы их решения");
            }
            case 4 -> {
                textField.setText(autofillVar);
                helpLabel.setText("Информация по автозаполнению полей");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelPanel = new javax.swing.JPanel();
        helpLabel = new javax.swing.JLabel();
        helpPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Справка");
        setLocation(new java.awt.Point(0, 0));
        setResizable(false);

        labelPanel.setBackground(new java.awt.Color(255, 255, 255));
        labelPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 0, 51)));

        helpLabel.setBackground(new java.awt.Color(255, 255, 255));
        helpLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        helpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        helpLabel.setText("Справочная информация");

        javax.swing.GroupLayout labelPanelLayout = new javax.swing.GroupLayout(labelPanel);
        labelPanel.setLayout(labelPanelLayout);
        labelPanelLayout.setHorizontalGroup(
            labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(labelPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addContainerGap())
        );
        labelPanelLayout.setVerticalGroup(
            labelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        helpPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(51, 0, 51)));

        javax.swing.GroupLayout helpPanelLayout = new javax.swing.GroupLayout(helpPanel);
        helpPanel.setLayout(helpPanelLayout);
        helpPanelLayout.setHorizontalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );
        helpPanelLayout.setVerticalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(helpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(labelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(helpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel helpLabel;
    private javax.swing.JPanel helpPanel;
    private javax.swing.JPanel labelPanel;
    // End of variables declaration//GEN-END:variables
}
