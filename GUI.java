import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;

//TODO Add BattleWeapon support
//TODO Add root file system support

public class GUI extends Application 
{
    boolean isFileOpened = false;
    Stage window;
    File givenFile;
    private ArrayList<UnitData> units = new ArrayList<UnitData>();
    private ListView<BattleUnitKind> kindList = null;
    private ListView<BattleUnitDefense> defenseList = null;
    private ListView<BattleUnitDefenseAttr> defenseAttrList = null;
    private ListView<StatusVulnerability> statuslist = null;
    private ListView<BattleWeapon> weaponList = null;

    private TextField HPField = new TextField();
    private TextField levelField = new TextField();
    private TextField bonusXPField = new TextField();
    private TextField bonusCoinField = new TextField();
    private TextField bonusCoinRateField = new TextField();
    private TextField baseCoinField = new TextField();
    private TextField runRateField = new TextField();
    private TextField pbCapField = new TextField();

    private TextField NormalField = new TextField();
    private TextField FireField = new TextField();
    private TextField IceField = new TextField();
    private TextField ExplosionField = new TextField();
    private TextField ElectricField = new TextField();

    ComboBox<String> NormalSelector = new ComboBox<>();
    ComboBox<String> FireSelector = new ComboBox<>();
    ComboBox<String> IceSelector = new ComboBox<>();
    ComboBox<String> ExplosionSelector = new ComboBox<>();
    ComboBox<String> ElectricSelector = new ComboBox<>();

    private TextField SleepField = new TextField();
    private TextField StopField = new TextField();
    private TextField DizzyField = new TextField();
    private TextField PoisonField = new TextField();
    private TextField ConfuseField = new TextField();
    //ElectricField borrowed from Defense
    private TextField BurnField = new TextField();
    private TextField FreezeField = new TextField();
    private TextField HugeField = new TextField();
    private TextField TinyField = new TextField();
    private TextField AttackUpField = new TextField();
    private TextField AttackDownField = new TextField();
    private TextField DefenseUpField = new TextField();
    private TextField DefenseDownField = new TextField();
    private TextField AllergicField = new TextField();
    private TextField FrightField = new TextField();
    private TextField GaleForceField = new TextField();
    private TextField FastField = new TextField();
    private TextField SlowField = new TextField();
    private TextField DodgyField = new TextField();
    private TextField InvisibleField = new TextField();
    private TextField OHKOField = new TextField();

    @Override
    public void start(Stage primaryStage) 
    {
        //Window
        window = primaryStage;
        window.setTitle("Battle Unit Tool");

        //Menu Buttons
        HBox topMenu = new HBox();
        Button openButton = new Button("Open");
        Button exportButton = new Button("Export");
        Button closeButton = new Button("Close");
        Button aboutButton = new Button("About");
        topMenu.getChildren().addAll(openButton, exportButton, closeButton, aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

        //Alligning Menu Buttons to Top
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);

        //Scene
        Scene emptyScene = new Scene(borderPane, 800, 600);
        window.setScene(emptyScene);

        openButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select a File to Open");
                givenFile = fileChooser.showOpenDialog(window);

                units = Main.determineFile(givenFile);
                isFileOpened = true;
                test.testUnitData(units);

                VBox leftMenu = new VBox();
                ComboBox<String> unitSelector = new ComboBox<>();
                for (UnitData unit : units) 
                {
                    unitSelector.getItems().add(unit.name);
                }

                //Setting struct names for BattleUnitKind
                kindList = new ListView<>();
                kindList.setCellFactory(lv -> new ListCell<BattleUnitKind>() 
                {
                    @Override protected void updateItem(BattleUnitKind item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null) 
                        {
                            setText(null);
                        } 
                        else 
                        {
                            int index = getIndex() + 1;
                            setText("Struct " + index);
                        }
                    }
                });

                //Setting struct names for BattleUnitDefense
                defenseList = new ListView<>();
                defenseList.setCellFactory(lv -> new ListCell<BattleUnitDefense>() 
                {
                    @Override protected void updateItem(BattleUnitDefense item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null) 
                        {
                            setText(null);
                        } 
                        else 
                        {
                            int index = getIndex() + 1;
                            setText("Struct " + index);
                        }
                    }
                });

                //Setting struct names for BattleUnitDefenseAttr
                defenseAttrList = new ListView<>();
                defenseAttrList.setCellFactory(lv -> new ListCell<BattleUnitDefenseAttr>() 
                {
                    @Override protected void updateItem(BattleUnitDefenseAttr item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null) 
                        {
                            setText(null);
                        } 
                        else 
                        {
                            int index = getIndex() + 1;
                            setText("Struct " + index);
                        }
                    }
                });

                //Setting struct names for StatusVulnerability
                statuslist = new ListView<>();
                statuslist.setCellFactory(lv -> new ListCell<StatusVulnerability>() 
                {
                    @Override protected void updateItem(StatusVulnerability item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null) 
                        {
                            setText(null);
                        } 
                        else 
                        {
                            int index = getIndex() + 1;
                            setText("Struct " + index);
                        }
                    }
                });

                weaponList = new ListView<>();
                
                leftMenu.getChildren().addAll(new Label(givenFile.getName()), unitSelector, new Label("BattleUnitKind:"), kindList, new Label("BattleUnitDefense:"), defenseList, new Label("BattleUnitDefenseAttr:"), defenseAttrList, new Label("StatusVulnerability:"), statuslist, new Label("BattleWeapon:"), weaponList);
                leftMenu.setPadding(new Insets(5));
                leftMenu.setSpacing(3);
                borderPane.setLeft(leftMenu);

                Button saveChangesButton = new Button("Save Changes");
                GridPane form = new GridPane();
                HBox centerMenu = new HBox();

                unitSelector.setOnAction(e -> 
                {
                    centerMenu.getChildren().clear();
                    loadSelectedUnit(unitSelector.getSelectionModel().getSelectedIndex());
                });

                kindList.setOnMouseClicked(e -> 
                {                    
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();

                    saveChangesButton.setText("Save Changes");

                    BattleUnitKind selected = kindList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(new Label("HP:"), 0, 0);
                    form.add(HPField, 1, 0);
                    form.add(new Label("Level:"), 0, 1);
                    form.add(levelField, 1, 1);
                    form.add(new Label("Bonus XP:"), 0, 2);
                    form.add(bonusXPField, 1, 2);
                    form.add(new Label("Bonus Coin:"), 0, 3);
                    form.add(bonusCoinField, 1, 3);
                    form.add(new Label("Bonus Coin Rate:"), 0, 4);
                    form.add(bonusCoinRateField, 1, 4);
                    form.add(new Label("Base Coin:"), 0, 5);
                    form.add(baseCoinField, 1, 5);
                    form.add(new Label("Run Rate:"), 0, 6);
                    form.add(runRateField, 1, 6);
                    form.add(new Label("PB Cap:"), 0, 7);
                    form.add(pbCapField, 1, 7);
                    form.add(saveChangesButton, 1, 8);

                    saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override public void handle(ActionEvent event)
                        {
                            saveFieldsToSelectedStruct();
                            saveChangesButton.setText("Changes Saved");
                        }
                    });

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });  
                defenseList.setOnMouseClicked(e -> 
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();

                    saveChangesButton.setText("Save Changes");

                    BattleUnitDefense selected = defenseList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(new Label("Normal Defense:"), 0, 0);
                    form.add(NormalField, 1, 0);
                    form.add(new Label("Fire Defense:"), 0, 1);
                    form.add(FireField, 1, 1);
                    form.add(new Label("Ice Defense:"), 0, 2);
                    form.add(IceField, 1, 2);
                    form.add(new Label("Explosion Defense:"), 0, 3);
                    form.add(ExplosionField, 1, 3);
                    form.add(new Label("Electric Defense:"), 0, 4);
                    form.add(ElectricField, 1, 4);
                    form.add(saveChangesButton, 1, 5);

                    saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override public void handle(ActionEvent event)
                        {
                            saveFieldsToSelectedStruct();
                            saveChangesButton.setText("Changes Saved");
                        }
                    });

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });
                defenseAttrList.setOnMouseClicked(e -> 
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();

                    NormalSelector.getItems().clear();
                    FireSelector.getItems().clear();
                    IceSelector.getItems().clear();
                    ExplosionSelector.getItems().clear();
                    ElectricSelector.getItems().clear();

                    NormalSelector.getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    FireSelector.getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    IceSelector.getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    ExplosionSelector.getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    ElectricSelector.getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");

                    saveChangesButton.setText("Save Changes");

                    BattleUnitDefenseAttr selected = defenseAttrList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(new Label("Normal Attribute Defense:"), 0, 0);
                    form.add(NormalSelector, 1, 0);
                    form.add(new Label("Fire Attribute Defense:"), 0, 1);
                    form.add(FireSelector, 1, 1);
                    form.add(new Label("Ice Attribute Defense:"), 0, 2);
                    form.add(IceSelector, 1, 2);
                    form.add(new Label("Explosion Attribute Defense:"), 0, 3);
                    form.add(ExplosionSelector, 1, 3);
                    form.add(new Label("Electric Attribute Defense:"), 0, 4);
                    form.add(ElectricSelector, 1, 4);
                    form.add(saveChangesButton, 1, 5);

                    saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override public void handle(ActionEvent event)
                        {
                            saveFieldsToSelectedStruct();
                            saveChangesButton.setText("Changes Saved");
                        }
                    });

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });
                statuslist.setOnMouseClicked(e -> 
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();

                    saveChangesButton.setText("Save Changes");

                    StatusVulnerability selected = statuslist.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(new Label("Sleep Susceptibility:"), 0, 0);
                    form.add(SleepField, 1, 0);
                    form.add(new Label("Stop Susceptibility:"), 0, 1);
                    form.add(StopField, 1, 1);
                    form.add(new Label("Dizzy Susceptibility:"), 0, 2);
                    form.add(DizzyField, 1, 2);
                    form.add(new Label("Poison Susceptibility:"), 0, 3);
                    form.add(PoisonField, 1, 3);
                    form.add(new Label("Confuse Susceptibility:"), 0, 4);
                    form.add(ConfuseField, 1, 4);
                    form.add(new Label("Electric Susceptibility:"), 0, 5);
                    form.add(ElectricField, 1, 5);
                    form.add(new Label("Burn Susceptibility:"), 0, 6);
                    form.add(BurnField, 1, 6);
                    form.add(new Label("Freeze Susceptibility:"), 0, 7);
                    form.add(FreezeField, 1, 7);
                    form.add(new Label("Huge Susceptibility:"), 0, 8);
                    form.add(HugeField, 1, 8);
                    form.add(new Label("Tiny Susceptibility:"), 0, 9);
                    form.add(TinyField, 1, 9);
                    form.add(new Label("Attack Up Susceptibility:"), 0, 10);
                    form.add(AttackUpField, 1, 10);
                    form.add(new Label("Attack Down Susceptibility:"), 0, 11);
                    form.add(AttackDownField, 1, 11);
                    form.add(new Label("Defense Up Susceptibility:"), 0, 12);
                    form.add(DefenseUpField, 1, 12);
                    form.add(new Label("Defense Down Susceptibility:"), 0, 13);
                    form.add(DefenseDownField, 1, 13);
                    form.add(new Label("Allergic Susceptibility:"), 0, 14);
                    form.add(AllergicField, 1, 14);
                    form.add(new Label("Fright Susceptibility:"), 0, 15);
                    form.add(FrightField, 1, 15);
                    form.add(new Label("Gale Force Susceptibility:"), 0, 16);
                    form.add(GaleForceField, 1, 16);
                    form.add(new Label("Fast Susceptibility:"), 0, 17);
                    form.add(FastField, 1, 17);
                    form.add(new Label("Slow Susceptibility:"), 0, 18);
                    form.add(SlowField, 1, 18);
                    form.add(new Label("Dodgy Susceptibility:"), 0, 19);
                    form.add(DodgyField, 1, 19);
                    form.add(new Label("Invisible Susceptibility:"), 0, 20);
                    form.add(InvisibleField, 1, 20);
                    form.add(new Label("OHKO Susceptibility:"), 0, 21);
                    form.add(OHKOField, 1, 21);
                    form.add(saveChangesButton, 1, 22);

                    saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override public void handle(ActionEvent event)
                        {
                            saveFieldsToSelectedStruct();
                            saveChangesButton.setText("Changes Saved");
                        }
                    });

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });
            }
        });

        exportButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                if(isFileOpened)
                {
                    byte[] newFileData = Main.buildNewFile(givenFile, units);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Save As");
                    File dest = fileChooser.showSaveDialog(window);
                    if (dest != null) 
                    {
                        try 
                        {
                            FileOutputStream fos = new FileOutputStream(dest);
                            fos.write(newFileData);
                            fos.close();

                            Stage successBox = new Stage();
                            successBox.setTitle("Export");

                            VBox successMenu = new VBox();
                            Text message = new Text("Successfully Saved!");
                            message.setWrappingWidth(290);
                            message.setTextAlignment(TextAlignment.CENTER);
                            successMenu.getChildren().addAll(new Label(""), message);

                            StackPane successPane = new StackPane();
                            successPane.getChildren().add(successMenu);
                            successPane.setAlignment(Pos.CENTER);

                            Scene successScene = new Scene(successPane, 150, 50);

                            successBox.setScene(successScene);
                            successBox.initModality(Modality.APPLICATION_MODAL);
                            successBox.show();
                        } 
                        catch (IOException ex) 
                        {
                            System.out.println("There was an error creating the output file");
                        }
                    }
                }
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                VBox emptyLeft = new VBox();
                borderPane.setLeft(emptyLeft);
                HBox emptyCenter = new HBox();
                borderPane.setCenter(emptyCenter);

                isFileOpened = false;
            }
        });

        aboutButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                Stage alertBox = new Stage();
                alertBox.setTitle("About");

                VBox alertMenu = new VBox();
                Text version = new Text("Written by Jemaroo     Version: 1.1.1");
                version.setWrappingWidth(290);
                version.setTextAlignment(TextAlignment.CENTER);
                Text description = new Text("TTYD BattleUnitTool allows you to open up the game's main dol or any rel file containing battle data and edit enemy unit fields.");
                description.setWrappingWidth(290);
                description.setTextAlignment(TextAlignment.CENTER);
                alertMenu.getChildren().addAll(new Label(""), version, new Label(""), description);

                StackPane alertPane = new StackPane();
                alertPane.getChildren().add(alertMenu);
                alertPane.setAlignment(Pos.CENTER);

                Scene alertScene = new Scene(alertPane, 300, 110);

                alertBox.setScene(alertScene);
                alertBox.initModality(Modality.APPLICATION_MODAL);
                alertBox.show();
            }
        });

        window.show();
    }

    /**
     * @Author Jemaroo
     * @Function Loads selected Unit Data into gui lists
     */
    private void loadSelectedUnit(int index) 
    {
        if (index >= 0 && index < units.size()) 
        {
            kindList.getItems().clear();
            defenseList.getItems().clear();
            defenseAttrList.getItems().clear();
            statuslist.getItems().clear();

            UnitData unit = units.get(index);
            kindList.getItems().setAll(unit.BattleUnitKindData);
            defenseList.getItems().setAll(unit.BattleUnitDefenseData);
            defenseAttrList.getItems().setAll(unit.BattleUnitDefenseAttrData);
            statuslist.getItems().setAll(unit.StatusVulnerabilityData);
        }
    }

    /**
     * @Author Jemaroo
     * @Function Loads BattleUnit Array Data into text fields
     */
    private void loadStructFields(Object struct) 
    {
        if (struct instanceof BattleUnitKind) 
        {
            BattleUnitKind b = (BattleUnitKind) struct;
            HPField.setText(String.valueOf(b.HP));
            levelField.setText(String.valueOf(b.level));
            bonusXPField.setText(String.valueOf(b.bonusXP));
            bonusCoinField.setText(String.valueOf(b.bonusCoin));
            bonusCoinRateField.setText(String.valueOf(b.bonusCoinRate));
            baseCoinField.setText(String.valueOf(b.baseCoin));
            runRateField.setText(String.valueOf(b.runRate));
            pbCapField.setText(String.valueOf(b.pbCap));
        } 
        else if (struct instanceof BattleUnitDefense) 
        {
            BattleUnitDefense b = (BattleUnitDefense) struct;
            NormalField.setText(String.valueOf(b.normal));
            FireField.setText(String.valueOf(b.fire));
            IceField.setText(String.valueOf(b.ice));
            ExplosionField.setText(String.valueOf(b.explosion));
            ElectricField.setText(String.valueOf(b.electric));
        }
        else if (struct instanceof BattleUnitDefenseAttr)
        {
            BattleUnitDefenseAttr b = (BattleUnitDefenseAttr) struct;
            NormalSelector.getSelectionModel().select(b.normal);
            FireSelector.getSelectionModel().select(b.fire);
            IceSelector.getSelectionModel().select(b.ice);
            ExplosionSelector.getSelectionModel().select(b.explosion);
            ElectricSelector.getSelectionModel().select(b.electric);
        }
        else if (struct instanceof StatusVulnerability)
        {
            StatusVulnerability b = (StatusVulnerability) struct;
            SleepField.setText(String.valueOf(b.sleep));
            StopField.setText(String.valueOf(b.stop));
            DizzyField.setText(String.valueOf(b.dizzy));
            PoisonField.setText(String.valueOf(b.poison));
            ConfuseField.setText(String.valueOf(b.confuse));
            ElectricField.setText(String.valueOf(b.electric));
            BurnField.setText(String.valueOf(b.burn));
            FreezeField.setText(String.valueOf(b.freeze));
            HugeField.setText(String.valueOf(b.huge));
            TinyField.setText(String.valueOf(b.tiny));
            AttackUpField.setText(String.valueOf(b.attack_up));
            AttackDownField.setText(String.valueOf(b.attack_down));
            DefenseUpField.setText(String.valueOf(b.defense_up));
            DefenseDownField.setText(String.valueOf(b.defense_down));
            AllergicField.setText(String.valueOf(b.allergic));
            FrightField.setText(String.valueOf(b.fright));
            GaleForceField.setText(String.valueOf(b.gale_force));
            FastField.setText(String.valueOf(b.fast));
            SlowField.setText(String.valueOf(b.slow));
            DodgyField.setText(String.valueOf(b.dodgy));
            InvisibleField.setText(String.valueOf(b.invisible));
            OHKOField.setText(String.valueOf(b.ohko));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Saves the text fields to the loaded BattleUnit Data
     */
    private void saveFieldsToSelectedStruct() 
    {
        Object selected = null;
        if (kindList.getSelectionModel().getSelectedItem() != null) 
        {
            selected = kindList.getSelectionModel().getSelectedItem();
        } 
        else if (defenseList.getSelectionModel().getSelectedItem() != null) 
        {
            selected = defenseList.getSelectionModel().getSelectedItem();
        } 
        else if (defenseAttrList.getSelectionModel().getSelectedItem() != null) 
        {
            selected = defenseAttrList.getSelectionModel().getSelectedItem();
        }
        else if (statuslist.getSelectionModel().getSelectedItem() != null)
        {
            selected = statuslist.getSelectionModel().getSelectedItem();
        }

        if (selected instanceof BattleUnitKind b) 
        {
            b.HP = Integer.parseInt(HPField.getText());
            b.level = Integer.parseInt(levelField.getText());
            b.bonusXP = Integer.parseInt(bonusXPField.getText());
            b.bonusCoin = Integer.parseInt(bonusCoinField.getText());
            b.bonusCoinRate = Integer.parseInt(bonusCoinRateField.getText());
            b.baseCoin = Integer.parseInt(baseCoinField.getText());
            b.runRate = Integer.parseInt(runRateField.getText());
            b.pbCap = Integer.parseInt(pbCapField.getText());
        } 
        else if (selected instanceof BattleUnitDefense b) 
        {
            b.normal = Integer.parseInt(NormalField.getText());
            b.fire = Integer.parseInt(FireField.getText());
            b.ice = Integer.parseInt(IceField.getText());
            b.explosion = Integer.parseInt(ExplosionField.getText());
            b.electric = Integer.parseInt(ElectricField.getText());
        }
        else if (selected instanceof BattleUnitDefenseAttr b)
        {
            b.normal = NormalSelector.getSelectionModel().getSelectedIndex();
            b.fire = FireSelector.getSelectionModel().getSelectedIndex();
            b.ice = IceSelector.getSelectionModel().getSelectedIndex();
            b.explosion = ExplosionSelector.getSelectionModel().getSelectedIndex();
            b.electric = ElectricSelector.getSelectionModel().getSelectedIndex();
        }
        else if (selected instanceof StatusVulnerability b)
        {
            b.sleep = Integer.parseInt(SleepField.getText());
            b.stop = Integer.parseInt(StopField.getText());
            b.dizzy = Integer.parseInt(DizzyField.getText());
            b.poison = Integer.parseInt(PoisonField.getText());
            b.confuse = Integer.parseInt(ConfuseField.getText());
            b.electric = Integer.parseInt(ElectricField.getText());
            b.burn = Integer.parseInt(BurnField.getText());
            b.freeze = Integer.parseInt(FreezeField.getText());
            b.huge = Integer.parseInt(HugeField.getText());
            b.tiny = Integer.parseInt(TinyField.getText());
            b.attack_up = Integer.parseInt(AttackUpField.getText());
            b.attack_down = Integer.parseInt(AttackDownField.getText());
            b.defense_up = Integer.parseInt(DefenseUpField.getText());
            b.defense_down = Integer.parseInt(DefenseDownField.getText());
            b.allergic = Integer.parseInt(AllergicField.getText());
            b.fright = Integer.parseInt(FrightField.getText());
            b.gale_force = Integer.parseInt(GaleForceField.getText());
            b.fast = Integer.parseInt(FastField.getText());
            b.slow = Integer.parseInt(SlowField.getText());
            b.dodgy = Integer.parseInt(DodgyField.getText());
            b.invisible = Integer.parseInt(InvisibleField.getText());
            b.ohko = Integer.parseInt(OHKOField.getText());
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}