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

//TODO Add BattleUnitKindPart support
//TODO Move Save Changes Button?

public class GUI extends Application 
{
    private ArrayList<String> validFileNames = new ArrayList<String>();
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

    private TextField AccuracyField = new TextField();
    private TextField FPCostField = new TextField();
    private TextField SPCostField = new TextField();
    ComboBox<String> SuperguardStateSelector = new ComboBox<>();
    private TextField StylishMultiplierField = new TextField();
    private TextField BingoSlotIncChanceField = new TextField();
    private TextField BaseDamage1Field = new TextField();
    private TextField BaseDamage2Field = new TextField();
    private TextField BaseDamage3Field = new TextField();
    private TextField BaseDamage4Field = new TextField();
    private TextField BaseDamage5Field = new TextField();
    private TextField BaseDamage6Field = new TextField();
    private TextField BaseDamage7Field = new TextField();
    private TextField BaseDamage8Field = new TextField();
    private TextField BaseFPDamage1Field = new TextField();
    private TextField BaseFPDamage2Field = new TextField();
    private TextField BaseFPDamage3Field = new TextField();
    private TextField BaseFPDamage4Field = new TextField();
    private TextField BaseFPDamage5Field = new TextField();
    private TextField BaseFPDamage6Field = new TextField();
    private TextField BaseFPDamage7Field = new TextField();
    private TextField BaseFPDamage8Field = new TextField();
    private CheckBox TCF1Box = new CheckBox();
    private CheckBox TCF2Box = new CheckBox();
    private CheckBox TCF3Box = new CheckBox();
    private CheckBox TCF4Box = new CheckBox();
    private CheckBox TCF5Box = new CheckBox();
    private CheckBox TCF6Box = new CheckBox();
    private CheckBox TCF7Box = new CheckBox();
    private CheckBox TCF8Box = new CheckBox();
    private CheckBox TCF9Box = new CheckBox();
    private CheckBox TCF10Box = new CheckBox();
    private CheckBox TCF11Box = new CheckBox();
    private CheckBox TPF1Box = new CheckBox();
    private CheckBox TPF2Box = new CheckBox();
    private CheckBox TPF3Box = new CheckBox();
    private CheckBox TPF4Box = new CheckBox();
    private CheckBox TPF5Box = new CheckBox();
    private CheckBox TPF6Box = new CheckBox();
    private CheckBox TPF7Box = new CheckBox();
    private CheckBox TPF8Box = new CheckBox();
    private CheckBox TPF9Box = new CheckBox();
    private CheckBox TPF10Box = new CheckBox();
    private CheckBox TPF11Box = new CheckBox();
    ComboBox<String> ElementSelector = new ComboBox<>();
    ComboBox<String> DamagePatternSelector = new ComboBox<>();
    private CheckBox SPF1Box = new CheckBox();
    private CheckBox SPF2Box = new CheckBox();
    private CheckBox SPF3Box = new CheckBox();
    private CheckBox SPF4Box = new CheckBox();
    private CheckBox SPF5Box = new CheckBox();
    private CheckBox SPF6Box = new CheckBox();
    private CheckBox SPF7Box = new CheckBox();
    private CheckBox SPF8Box = new CheckBox();
    private CheckBox SPF9Box = new CheckBox();
    private CheckBox SPF10Box = new CheckBox();
    private CheckBox SPF11Box = new CheckBox();
    private CheckBox SPF12Box = new CheckBox();
    private CheckBox SPF13Box = new CheckBox();
    private CheckBox SPF14Box = new CheckBox();
    private CheckBox CRF1Box = new CheckBox();
    private CheckBox CRF2Box = new CheckBox();
    private CheckBox CRF3Box = new CheckBox();
    private CheckBox CRF4Box = new CheckBox();
    private CheckBox CRF5Box = new CheckBox();
    private CheckBox CRF6Box = new CheckBox();
    private CheckBox CRF7Box = new CheckBox();
    private CheckBox CRF8Box = new CheckBox();
    private CheckBox CRF9Box = new CheckBox();
    private CheckBox CRF10Box = new CheckBox();
    private CheckBox CRF11Box = new CheckBox();
    private CheckBox TWF1Box = new CheckBox();
    private CheckBox TWF2Box = new CheckBox();
    private CheckBox TWF3Box = new CheckBox();
    private CheckBox TWF4Box = new CheckBox();
    private CheckBox TWF5Box = new CheckBox();
    private CheckBox TWF6Box = new CheckBox();
    private CheckBox TWF7Box = new CheckBox();
    private CheckBox TWF8Box = new CheckBox();
    private CheckBox TWF9Box = new CheckBox();
    private CheckBox TWF10Box = new CheckBox();
    private CheckBox TWF11Box = new CheckBox();
    private CheckBox TWF12Box = new CheckBox();
    private TextField SleepChanceField = new TextField();
    private TextField SleepTimeField = new TextField();
    private TextField StopChanceField = new TextField();
    private TextField StopTimeField = new TextField();
    private TextField DizzyChanceField = new TextField();
    private TextField DizzyTimeField = new TextField();
    private TextField PoisonChanceField = new TextField();
    private TextField PoisonTimeField = new TextField();
    private TextField PoisonStrengthField = new TextField();
    private TextField ConfuseChanceField = new TextField();
    private TextField ConfuseTimeField = new TextField();
    private TextField ElectricChanceField = new TextField();
    private TextField ElectricTimeField = new TextField();
    private TextField DodgyChanceField = new TextField();
    private TextField DodgyTimeField = new TextField();
    private TextField BurnChanceField = new TextField();
    private TextField BurnTimeField = new TextField();
    private TextField FreezeChanceField = new TextField();
    private TextField FreezeTimeField = new TextField();
    private TextField SizeChangeChanceField = new TextField();
    private TextField SizeChangeTimeField = new TextField();
    private TextField SizeChangeStrengthField = new TextField();
    private TextField AtkChangeChanceField = new TextField();
    private TextField AtkChangeTimeField = new TextField();
    private TextField AtkChangeStrengthField = new TextField();
    private TextField DefChangeChanceField = new TextField();
    private TextField DefChangeTimeField = new TextField();
    private TextField DefChangeStrengthField = new TextField();
    private TextField AllergicChanceField = new TextField();
    private TextField AllergicTimeField = new TextField();
    private TextField OHKOChanceField = new TextField();
    private TextField ChargeStrengthField = new TextField();
    private TextField FastChanceField = new TextField();
    private TextField FastTimeField = new TextField();
    private TextField SlowChanceField = new TextField();
    private TextField SlowTimeField = new TextField();
    private TextField FrightChanceField = new TextField();
    private TextField GaleForceChanceField = new TextField();
    private TextField PaybackTimeField = new TextField();
    private TextField HoldFastTimeField = new TextField();
    private TextField InvisibleChanceField = new TextField();
    private TextField InvisibleTimeField = new TextField();
    private TextField HPRegenTimeField = new TextField();
    private TextField HPRegenStrengthField = new TextField();
    private TextField FPRegenTimeField = new TextField();
    private TextField FPRegenStrengthField = new TextField();
    private TextField STGFall1Field = new TextField();
    private TextField STGFall2Field = new TextField();
    private TextField STGFall3Field = new TextField();
    private TextField STGFall4Field = new TextField();
    private TextField STGNozzleTurnField = new TextField();
    private TextField STGNozzleFireField = new TextField();
    private TextField STGCeilingFallField = new TextField();
    private TextField STGObjectFallField = new TextField();

    @Override
    public void start(Stage primaryStage) 
    {
        String[] tempValidFileNames = {"main.dol", "Start.dol", "aji.rel", "bom.rel", "dou.rel", "eki.rel", "gon.rel", "gor.rel", "gra.rel", "hei.rel", "jin.rel", "jon.rel", "las.rel", "moo.rel", "mri.rel", "muj.rel", "nok.rel", "pik.rel", "rsh.rel", "tik.rel", "tou2.rel", "win.rel", "mod.rel", "hm_enemies.rel"};
        for(int i = 0; i < tempValidFileNames.length; i++)
        {
            validFileNames.add(tempValidFileNames[i]);
        }

        //Window
        window = primaryStage;
        window.setTitle("Battle Unit Tool");

        //Menu Buttons
        HBox topMenu = new HBox();
        HBox centerMenu = new HBox();
        Button openButton = new Button("Open");
        ComboBox<File> fileSelector = new ComboBox<>();
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
                fileSelector.getItems().clear();
                topMenu.getChildren().clear();
                topMenu.getChildren().addAll(openButton, fileSelector, exportButton, closeButton, aboutButton);

                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select the root folder");
                givenFile = directoryChooser.showDialog(window);

                ArrayList<File> validFiles = Main.findMatchingFiles(givenFile, validFileNames);

                for (File f : validFiles)
                {
                    fileSelector.getItems().add(f);
                }

                //Setting list names for Directory Chooser
                fileSelector.setCellFactory(lv -> new ListCell<File>() 
                {
                    @Override protected void updateItem(File item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null) 
                        {
                            setText(null);
                        } 
                        else 
                        {
                            setText(item.getName());
                        }
                    }
                });

                fileSelector.setOnAction(e -> 
                {
                    centerMenu.getChildren().clear();

                    units = Main.getTableData(fileSelector.getSelectionModel().getSelectedItem());
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

                    //Setting struct names for StatusVulnerability
                    weaponList = new ListView<>();
                    weaponList.setCellFactory(lv -> new ListCell<BattleWeapon>() 
                    {
                        @Override protected void updateItem(BattleWeapon item, boolean empty) 
                        {
                            super.updateItem(item, empty);
                            if (empty || item == null) 
                            {
                                setText(null);
                            } 
                            else 
                            {
                                setText(item.attackName);
                            }
                        }
                    });
                    
                    leftMenu.getChildren().addAll(unitSelector, new Label("BattleUnitKind:"), kindList, new Label("BattleUnitDefense:"), defenseList, new Label("BattleUnitDefenseAttr:"), defenseAttrList, new Label("StatusVulnerability:"), statuslist, new Label("BattleWeapon:"), weaponList);
                    leftMenu.setPadding(new Insets(5));
                    leftMenu.setSpacing(3);
                    borderPane.setLeft(leftMenu);

                    Button saveChangesButton = new Button("Save Changes");
                    GridPane form = new GridPane();

                    unitSelector.setOnAction(e2 -> 
                    {
                        centerMenu.getChildren().clear();
                        loadSelectedUnit(unitSelector.getSelectionModel().getSelectedIndex());
                    });

                    kindList.setOnMouseClicked(e2 -> 
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
                    defenseList.setOnMouseClicked(e2 -> 
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
                    defenseAttrList.setOnMouseClicked(e2 -> 
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
                    statuslist.setOnMouseClicked(e2 -> 
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
                    weaponList.setOnMouseClicked(e2 -> 
                    {
                        centerMenu.getChildren().clear();
                        form.getChildren().clear();

                        SuperguardStateSelector.getItems().clear();
                        ElementSelector.getItems().clear();
                        DamagePatternSelector.getItems().clear();
                        SuperguardStateSelector.getItems().addAll("Unsuperguardable", "Superguardable with Recoil", "Superguardable without Recoil");
                        ElementSelector.getItems().addAll("Normal", "Fire", "Ice", "Explosive", "Electric");
                        DamagePatternSelector.getItems().addAll("None", "???", "Gulp Knocked onto Ground", "Gulp Knocked into next Target", "???", "Squashed Type 1", "Squashed Type 2", "Squashed Type 3", "Inked", "Super Hammer Knocked Back", "Ultra Hammer Knocked Back", "???", "Confusion Effect", "Spin Once 1", "Spin Once 2", "Quickly Spin 1", "Quickly Spin 2", "Blown Away", "???");

                        saveChangesButton.setText("Save Changes");

                        BattleWeapon selected = weaponList.getSelectionModel().getSelectedItem();

                        if (selected != null) loadStructFields(selected);

                        form.setVgap(5);
                        form.setHgap(10);
                        form.setPadding(new Insets(10));

                        form.add(new Label("Accuracy:"), 0, 0);
                        form.add(AccuracyField, 1, 0);
                        form.add(new Label("FP Cost:"), 0, 1);
                        form.add(FPCostField, 1, 1);
                        form.add(new Label("SP Cost:"), 0, 2);
                        form.add(SPCostField, 1, 2);
                        form.add(new Label("Superguard State:"), 0, 3);
                        form.add(SuperguardStateSelector, 1, 3);
                        form.add(new Label("Stylish Multiplier:"), 0, 4);
                        form.add(StylishMultiplierField, 1, 4);
                        form.add(new Label("Increase Bingo Slot Chance:"), 0, 5);
                        form.add(BingoSlotIncChanceField, 1, 5);
                        form.add(new Label("Base Damage Parameter 1:"), 0, 6);
                        form.add(BaseDamage1Field, 1, 6);
                        form.add(new Label("Base Damage Parameter 2:"), 0, 7);
                        form.add(BaseDamage2Field, 1, 7);
                        form.add(new Label("Base Damage Parameter 3:"), 0, 8);
                        form.add(BaseDamage3Field, 1, 8);
                        form.add(new Label("Base Damage Parameter 4:"), 0, 9);
                        form.add(BaseDamage4Field, 1, 9);
                        form.add(new Label("Base Damage Parameter 5:"), 0, 10);
                        form.add(BaseDamage5Field, 1, 10);
                        form.add(new Label("Base Damage Parameter 6:"), 0, 11);
                        form.add(BaseDamage6Field, 1, 11);
                        form.add(new Label("Base Damage Parameter 7:"), 0, 12);
                        form.add(BaseDamage7Field, 1, 12);
                        form.add(new Label("Base Damage Parameter 8:"), 0, 13);
                        form.add(BaseDamage8Field, 1, 13);
                        form.add(new Label("Base FP Damage Parameter 1:"), 0, 14);
                        form.add(BaseFPDamage1Field, 1, 14);
                        form.add(new Label("Base FP Damage Parameter 2:"), 0, 15);
                        form.add(BaseFPDamage2Field, 1, 15);
                        form.add(new Label("Base FP Damage Parameter 3:"), 0, 16);
                        form.add(BaseFPDamage3Field, 1, 16);
                        form.add(new Label("Base FP Damage Parameter 4:"), 0, 17);
                        form.add(BaseFPDamage4Field, 1, 17);
                        form.add(new Label("Base FP Damage Parameter 5:"), 0, 18);
                        form.add(BaseFPDamage5Field, 1, 18);
                        form.add(new Label("Base FP Damage Parameter 6:"), 0, 19);
                        form.add(BaseFPDamage6Field, 1, 19);
                        form.add(new Label("Base FP Damage Parameter 7:"), 0, 20);
                        form.add(BaseFPDamage7Field, 1, 20);
                        form.add(new Label("Base FP Damage Parameter 8:"), 0, 21);
                        form.add(BaseFPDamage8Field, 1, 21);
                        form.add(new Label("Element:"), 0, 22);
                        form.add(ElementSelector, 1, 22);
                        form.add(new Label("Damage Pattern:"), 0, 23);
                        form.add(DamagePatternSelector, 1, 23);
                        form.add(new Label("Sleep Chance:"), 0, 24);
                        form.add(SleepChanceField, 1, 24);
                        form.add(new Label("Sleep Time:"), 0, 25);
                        form.add(SleepTimeField, 1, 25);
                        form.add(new Label("Stop Chance:"), 0, 26);
                        form.add(StopChanceField, 1, 26);
                        form.add(new Label("Stop Time:"), 0, 27);
                        form.add(StopTimeField, 1, 27);
                        form.add(new Label("Dizzy Chance:"), 0, 28);
                        form.add(DizzyChanceField, 1, 28);
                        form.add(new Label("Dizzy Time:"), 0, 29);
                        form.add(DizzyTimeField, 1, 29);
                        form.add(new Label("Poison Chance:"), 0, 30);
                        form.add(PoisonChanceField, 1, 30);
                        form.add(new Label("Poison Time:"), 0, 31);
                        form.add(PoisonTimeField, 1, 31);
                        form.add(new Label("Poison Strength:"), 0, 32);
                        form.add(PoisonStrengthField, 1, 32);
                        form.add(new Label("Confuse Chance:"), 0, 33);
                        form.add(ConfuseChanceField, 1, 33);
                        form.add(new Label("Confuse Time:"), 0, 34);
                        form.add(ConfuseTimeField, 1, 34);
                        form.add(new Label("Electric Chance:"), 0, 35);
                        form.add(ElectricChanceField, 1, 35);
                        form.add(new Label("Electric Time:"), 0, 36);
                        form.add(ElectricTimeField, 1, 36);
                        form.add(new Label("Dodgy Chance:"), 0, 37);
                        form.add(DodgyChanceField, 1, 37);
                        form.add(new Label("Dodgy Time:"), 0, 38);
                        form.add(DodgyTimeField, 1, 38);
                        form.add(new Label("Burn Chance:"), 0, 39);
                        form.add(BurnChanceField, 1, 39);
                        form.add(new Label("Burn Time:"), 0, 40);
                        form.add(BurnTimeField, 1, 40);
                        form.add(new Label("Freeze Chance:"), 0, 41);
                        form.add(FreezeChanceField, 1, 41);
                        form.add(new Label("Freeze Time:"), 0, 42);
                        form.add(FreezeTimeField, 1, 42);
                        form.add(new Label("Size Change Chance:"), 0, 43);
                        form.add(SizeChangeChanceField, 1, 43);
                        form.add(new Label("Size Change Time:"), 0, 44);
                        form.add(SizeChangeTimeField, 1, 44);
                        form.add(new Label("Size Change Strength:"), 0, 45);
                        form.add(SizeChangeStrengthField, 1, 45);
                        form.add(new Label("Atk Change Chance:"), 0, 46);
                        form.add(AtkChangeChanceField, 1, 46);
                        form.add(new Label("Atk Change Time:"), 0, 47);
                        form.add(AtkChangeTimeField, 1, 47);
                        form.add(new Label("Atk Change Strength:"), 0, 48);
                        form.add(AtkChangeStrengthField, 1, 48);
                        form.add(new Label("Def Change Chance:"), 0, 49);
                        form.add(DefChangeChanceField, 1, 49);
                        form.add(new Label("Def Change Time:"), 0, 50);
                        form.add(DefChangeTimeField, 1, 50);
                        form.add(new Label("Def Change Strength:"), 0, 51);
                        form.add(DefChangeStrengthField, 1, 51);
                        form.add(new Label("Allergic Chance:"), 0, 52);
                        form.add(AllergicChanceField, 1, 52);
                        form.add(new Label("Allergic Time:"), 0, 53);
                        form.add(AllergicTimeField, 1, 53);
                        form.add(new Label("OHKO Chance:"), 0, 54);
                        form.add(OHKOChanceField, 1, 54);
                        form.add(new Label("Charge Strength:"), 0, 55);
                        form.add(ChargeStrengthField, 1, 55);
                        form.add(new Label("Fast Chance:"), 0, 56);
                        form.add(FastChanceField, 1, 56);
                        form.add(new Label("Fast Time:"), 0, 57);
                        form.add(FastTimeField, 1, 57);
                        form.add(new Label("Slow Chance:"), 0, 58);
                        form.add(SlowChanceField, 1, 58);
                        form.add(new Label("Slow Time:"), 0, 59);
                        form.add(SlowTimeField, 1, 59);
                        form.add(new Label("Fright Chance:"), 0, 60);
                        form.add(FrightChanceField, 1, 60);
                        form.add(new Label("Gale Force Chance:"), 0, 61);
                        form.add(GaleForceChanceField, 1, 61);
                        form.add(new Label("Payback Time:"), 0, 62);
                        form.add(PaybackTimeField, 1, 62);
                        form.add(new Label("Hold Fast Time:"), 0, 63);
                        form.add(HoldFastTimeField, 1, 63);
                        form.add(new Label("Invisible Chance:"), 0, 64);
                        form.add(InvisibleChanceField, 1, 64);
                        form.add(new Label("Invisible Time:"), 0, 65);
                        form.add(InvisibleTimeField, 1, 65);
                        form.add(new Label("HP Regen Time:"), 0, 66);
                        form.add(HPRegenTimeField, 1, 66);
                        form.add(new Label("HP Regen Strength:"), 0, 67);
                        form.add(HPRegenStrengthField, 1, 67);
                        form.add(new Label("FP Regen Time:"), 0, 68);
                        form.add(FPRegenTimeField, 1, 68);
                        form.add(new Label("FP Regen Strength:"), 0, 69);
                        form.add(FPRegenStrengthField, 1, 69);
                        form.add(new Label("Stage Background Fall Chance 1:"), 0, 70);
                        form.add(STGFall1Field, 1, 70);
                        form.add(new Label("Stage Background Fall Chance 2:"), 0, 71);
                        form.add(STGFall2Field, 1, 71);
                        form.add(new Label("Stage Background Fall Chance 3:"), 0, 72);
                        form.add(STGFall3Field, 1, 72);
                        form.add(new Label("Stage Background Fall Chance 4:"), 0, 73);
                        form.add(STGFall4Field, 1, 73);
                        form.add(new Label("Stage Nozzle Turn Chance:"), 0, 74);
                        form.add(STGNozzleTurnField, 1, 74);
                        form.add(new Label("Stage Nozzle Fire Chance:"), 0, 75);
                        form.add(STGNozzleFireField, 1, 75);
                        form.add(new Label("Stage Ceiling Fall Chance:"), 0, 76);
                        form.add(STGCeilingFallField, 1, 76);
                        form.add(new Label("Stage Object Fall Chance:"), 0, 77);
                        form.add(STGObjectFallField, 1, 77);
                        form.add(new Label("Cannot Target Mario or Shell Shield:"), 0, 78);
                        form.add(TCF1Box, 1, 78);
                        form.add(new Label("Cannot Target Partner:"), 0, 79);
                        form.add(TCF2Box, 1, 79);
                        form.add(new Label("Cannot Target Enemy:"), 0, 80);
                        form.add(TCF3Box, 1, 80);
                        form.add(new Label("Cannot Target Opposite Alliance:"), 0, 81);
                        form.add(TCF4Box, 1, 81);
                        form.add(new Label("Cannot Target Own Alliance:"), 0, 82);
                        form.add(TCF5Box, 1, 82);
                        form.add(new Label("Cannot Target Self:"), 0, 83);
                        form.add(TCF6Box, 1, 83);
                        form.add(new Label("Cannot Target Same Species:"), 0, 84);
                        form.add(TCF7Box, 1, 84);
                        form.add(new Label("Only Target Self:"), 0, 85);
                        form.add(TCF8Box, 1, 85);
                        form.add(new Label("Only Target Mario:"), 0, 86);
                        form.add(TCF9Box, 1, 86);
                        form.add(new Label("Single Target:"), 0, 87);
                        form.add(TCF10Box, 1, 87);
                        form.add(new Label("Multiple Target:"), 0, 88);
                        form.add(TCF11Box, 1, 88);
                        form.add(new Label("Is Tattle-like:"), 0, 89);
                        form.add(TPF1Box, 1, 89);
                        form.add(new Label("Cannot Target Ceiling:"), 0, 90);
                        form.add(TPF2Box, 1, 90);
                        form.add(new Label("Cannot Target Floating:"), 0, 91);
                        form.add(TPF3Box, 1, 91);
                        form.add(new Label("Cannot Target Grounded:"), 0, 92);
                        form.add(TPF4Box, 1, 92);
                        form.add(new Label("Is Jump-like:"), 0, 93);
                        form.add(TPF5Box, 1, 93);
                        form.add(new Label("Is Hammer-like:"), 0, 94);
                        form.add(TPF6Box, 1, 94);
                        form.add(new Label("Is Shell Toss-like:"), 0, 95);
                        form.add(TPF7Box, 1, 95);
                        form.add(new Label("Has Recoil Damage:"), 0, 96);
                        form.add(TPF8Box, 1, 96);
                        form.add(new Label("Can Only Target Frontmost:"), 0, 97);
                        form.add(TPF9Box, 1, 97);
                        form.add(new Label("Target Same Alliance Direction:"), 0, 98);
                        form.add(TPF10Box, 1, 98);
                        form.add(new Label("Target Opposite Alliance Direction:"), 0, 99);
                        form.add(TPF11Box, 1, 99);
                        form.add(new Label("Badge Can Affect Power:"), 0, 100);
                        form.add(SPF1Box, 1, 100);
                        form.add(new Label("Status Can Affect Power:"), 0, 101);
                        form.add(SPF2Box, 1, 101);
                        form.add(new Label("Is Chargeable:"), 0, 102);
                        form.add(SPF3Box, 1, 102);
                        form.add(new Label("Cannot Miss:"), 0, 103);
                        form.add(SPF4Box, 1, 103);
                        form.add(new Label("Has Diminishing Damage by Hit:"), 0, 104);
                        form.add(SPF5Box, 1, 104);
                        form.add(new Label("Has Diminishing Damage by Target:"), 0, 105);
                        form.add(SPF6Box, 1, 105);
                        form.add(new Label("Pierces Defense:"), 0, 106);
                        form.add(SPF7Box, 1, 106);
                        form.add(new Label("Ingore Target Status Vulnerability:"), 0, 107);
                        form.add(SPF8Box, 1, 107);
                        form.add(new Label("Change Element to Fire if Target Burned:"), 0, 108);
                        form.add(SPF9Box, 1, 108);
                        form.add(new Label("Flips Shell Enemies:"), 0, 109);
                        form.add(SPF10Box, 1, 109);
                        form.add(new Label("Flips Bomb-Flippable Enemies:"), 0, 110);
                        form.add(SPF11Box, 1, 110);
                        form.add(new Label("Grounds Winged Enemies:"), 0, 111);
                        form.add(SPF12Box, 1, 111);
                        form.add(new Label("Can be Used when Confused:"), 0, 112);
                        form.add(SPF13Box, 1, 112);
                        form.add(new Label("Is Unguardable:"), 0, 113);
                        form.add(SPF14Box, 1, 113);
                        form.add(new Label("Ignore Electric Resistance:"), 0, 114);
                        form.add(CRF1Box, 1, 114);
                        form.add(new Label("Ignore Top Spiky Resistance:"), 0, 115);
                        form.add(CRF2Box, 1, 115);
                        form.add(new Label("Ignore Pre-emptive Front Spiky Resistance:"), 0, 116);
                        form.add(CRF3Box, 1, 116);
                        form.add(new Label("Ignore Front Spiky Resistance:"), 0, 117);
                        form.add(CRF4Box, 1, 117);
                        form.add(new Label("Ignore Fire Resistance:"), 0, 118);
                        form.add(CRF5Box, 1, 118);
                        form.add(new Label("Ignore Ice Resistance:"), 0, 119);
                        form.add(CRF6Box, 1, 119);
                        form.add(new Label("Ignore Poison Resistance:"), 0, 120);
                        form.add(CRF7Box, 1, 120);
                        form.add(new Label("Ignore Explosive Resistance:"), 0, 121);
                        form.add(CRF8Box, 1, 121);
                        form.add(new Label("Ignore Volatile Explosive Resistance:"), 0, 122);
                        form.add(CRF9Box, 1, 122);
                        form.add(new Label("Ignore Payback Resistance:"), 0, 123);
                        form.add(CRF10Box, 1, 123);
                        form.add(new Label("Ignore Hold Fast Resistance:"), 0, 124);
                        form.add(CRF11Box, 1, 124);
                        form.add(new Label("Prefer Targeting Mario:"), 0, 125);
                        form.add(TWF1Box, 1, 125);
                        form.add(new Label("Prefer Targeting Partner:"), 0, 126);
                        form.add(TWF2Box, 1, 126);
                        form.add(new Label("Prefer Targeting Front:"), 0, 127);
                        form.add(TWF3Box, 1, 127);
                        form.add(new Label("Prefer Targeting Back:"), 0, 128);
                        form.add(TWF4Box, 1, 128);
                        form.add(new Label("Prefer Targeting Same Alliance:"), 0, 129);
                        form.add(TWF5Box, 1, 129);
                        form.add(new Label("Prefer Targeting Opposite Alliance:"), 0, 130);
                        form.add(TWF6Box, 1, 130);
                        form.add(new Label("Prefer Targeting Less Healthy:"), 0, 131);
                        form.add(TWF7Box, 1, 131);
                        form.add(new Label("Greatly Prefer Targeting Less Healthy:"), 0, 132);
                        form.add(TWF8Box, 1, 132);
                        form.add(new Label("Prefer Targeting Lower HP Target:"), 0, 133);
                        form.add(TWF9Box, 1, 133);
                        form.add(new Label("Prefer Targeting Higher HP Target:"), 0, 134);
                        form.add(TWF10Box, 1, 134);
                        form.add(new Label("Prefer Targeting Target in Peril:"), 0, 135);
                        form.add(TWF11Box, 1, 135);
                        form.add(new Label("Choose Targeting Randomly:"), 0, 136);
                        form.add(TWF12Box, 1, 136);
                        form.add(saveChangesButton, 1, 137);

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
                });
            }
        });

        exportButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                if(isFileOpened)
                {
                    byte[] newFileData = Main.buildNewFile(fileSelector.getSelectionModel().getSelectedItem(), units);
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
                Text version = new Text("Written by Jemaroo     Version: 1.2.0");
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
            weaponList.getItems().clear();

            UnitData unit = units.get(index);
            kindList.getItems().setAll(unit.BattleUnitKindData);
            defenseList.getItems().setAll(unit.BattleUnitDefenseData);
            defenseAttrList.getItems().setAll(unit.BattleUnitDefenseAttrData);
            statuslist.getItems().setAll(unit.StatusVulnerabilityData);
            weaponList.getItems().setAll(unit.BattleWeaponData);
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
        else if (struct instanceof BattleWeapon)
        {
            BattleWeapon b = (BattleWeapon) struct;
            AccuracyField.setText(String.valueOf(b.accuracy));
            FPCostField.setText(String.valueOf(b.fp_cost));
            SPCostField.setText(String.valueOf(b.sp_cost));
            SuperguardStateSelector.getSelectionModel().select(b.superguard_state);
            StylishMultiplierField.setText(String.valueOf(b.sylish_multiplier));
            BingoSlotIncChanceField.setText(String.valueOf(b.bingo_slot_inc_chance));
            BaseDamage1Field.setText(String.valueOf(b.base_damage1));
            BaseDamage2Field.setText(String.valueOf(b.base_damage2));
            BaseDamage3Field.setText(String.valueOf(b.base_damage3));
            BaseDamage4Field.setText(String.valueOf(b.base_damage4));
            BaseDamage5Field.setText(String.valueOf(b.base_damage5));
            BaseDamage6Field.setText(String.valueOf(b.base_damage6));
            BaseDamage7Field.setText(String.valueOf(b.base_damage7));
            BaseDamage8Field.setText(String.valueOf(b.base_damage8));
            BaseFPDamage1Field.setText(String.valueOf(b.base_fpdamage1));
            BaseFPDamage2Field.setText(String.valueOf(b.base_fpdamage2));
            BaseFPDamage3Field.setText(String.valueOf(b.base_fpdamage3));
            BaseFPDamage4Field.setText(String.valueOf(b.base_fpdamage4));
            BaseFPDamage5Field.setText(String.valueOf(b.base_fpdamage5));
            BaseFPDamage6Field.setText(String.valueOf(b.base_fpdamage6));
            BaseFPDamage7Field.setText(String.valueOf(b.base_fpdamage7));
            BaseFPDamage8Field.setText(String.valueOf(b.base_fpdamage8));
            TCF1Box.setSelected(b.CannotTargetMarioOrShellShield);
            TCF2Box.setSelected(b.CannotTargetPartner);
            TCF3Box.setSelected(b.CannotTargetEnemy);
            TCF4Box.setSelected(b.CannotTargetOppositeAlliance);
            TCF5Box.setSelected(b.CannotTargetOwnAlliance);
            TCF6Box.setSelected(b.CannotTargetSelf);
            TCF7Box.setSelected(b.CannotTargetSameSpecies);
            TCF8Box.setSelected(b.OnlyTargetSelf);
            TCF9Box.setSelected(b.OnlyTargetMario);
            TCF10Box.setSelected(b.SingleTarget);
            TCF11Box.setSelected(b.MultipleTarget);
            TPF1Box.setSelected(b.Tattlelike);
            TPF2Box.setSelected(b.CannotTargetCeiling);
            TPF3Box.setSelected(b.CannotTargetFloating);
            TPF4Box.setSelected(b.CannotTargetGrounded);
            TPF5Box.setSelected(b.Jumplike);
            TPF6Box.setSelected(b.Hammerlike);
            TPF7Box.setSelected(b.ShellTosslike);
            TPF8Box.setSelected(b.RecoilDamage);
            TPF9Box.setSelected(b.CanOnlyTargetFrontmost);
            TPF10Box.setSelected(b.TargetSameAllianceDirection);
            TPF11Box.setSelected(b.TargetOppositeAllianceDirection);
            ElementSelector.getSelectionModel().select(b.element);
            DamagePatternSelector.getSelectionModel().select(DPtoIndex(b.damage_pattern));
            SPF1Box.setSelected(b.BadgeCanAffectPower);
            SPF2Box.setSelected(b.StatusCanAffectPower);
            SPF3Box.setSelected(b.IsChargeable);
            SPF4Box.setSelected(b.CannotMiss);
            SPF5Box.setSelected(b.DiminishingReturnsByHit);
            SPF6Box.setSelected(b.DiminishingReturnsByTarget);
            SPF7Box.setSelected(b.PiercesDefense);
            SPF8Box.setSelected(b.IgnoreTargetStatusVulnerability);
            SPF9Box.setSelected(b.IgnitesIfBurned);
            SPF10Box.setSelected(b.FlipsShellEnemies);
            SPF11Box.setSelected(b.FlipsBombFlippableEnemies);
            SPF12Box.setSelected(b.GroundsWingedEnemies);
            SPF13Box.setSelected(b.CanBeUsedAsConfusedAction);
            SPF14Box.setSelected(b.Unguardable);
            CRF1Box.setSelected(b.Electric);
            CRF2Box.setSelected(b.TopSpiky);
            CRF3Box.setSelected(b.PreemptiveFrontSpiky);
            CRF4Box.setSelected(b.FrontSpiky);
            CRF5Box.setSelected(b.Fiery);
            CRF6Box.setSelected(b.Icy);
            CRF7Box.setSelected(b.Poison);
            CRF8Box.setSelected(b.Explosive);
            CRF9Box.setSelected(b.VolatileExplosive);
            CRF10Box.setSelected(b.Payback);
            CRF11Box.setSelected(b.HoldFast);
            TWF1Box.setSelected(b.PreferMario);
            TWF1Box.setSelected(b.PreferPartner);
            TWF1Box.setSelected(b.PreferFront);
            TWF1Box.setSelected(b.PreferBack);
            TWF1Box.setSelected(b.PreferSameAlliance);
            TWF1Box.setSelected(b.PreferOppositeAlliance);
            TWF1Box.setSelected(b.PreferLessHealthy);
            TWF1Box.setSelected(b.GreatlyPreferLessHealthy);
            TWF1Box.setSelected(b.PreferLowerHP);
            TWF1Box.setSelected(b.PreferHigherHP);
            TWF1Box.setSelected(b.PreferInPeril);
            TWF1Box.setSelected(b.ChooseWeightedRandomly);
            SleepChanceField.setText(String.valueOf(b.sleep_chance));
            SleepTimeField.setText(String.valueOf(b.sleep_time));
            StopChanceField.setText(String.valueOf(b.stop_chance));
            StopTimeField.setText(String.valueOf(b.stop_time));
            DizzyChanceField.setText(String.valueOf(b.dizzy_chance));
            DizzyTimeField.setText(String.valueOf(b.dizzy_time));
            PoisonChanceField.setText(String.valueOf(b.poison_chance));
            PoisonTimeField.setText(String.valueOf(b.poison_time));
            PoisonStrengthField.setText(String.valueOf(b.poison_strength));
            ConfuseChanceField.setText(String.valueOf(b.confuse_chance));
            ConfuseTimeField.setText(String.valueOf(b.confuse_time));
            ElectricChanceField.setText(String.valueOf(b.electric_chance));
            ElectricTimeField.setText(String.valueOf(b.electric_time));
            DodgyChanceField.setText(String.valueOf(b.dodgy_chance));
            DodgyTimeField.setText(String.valueOf(b.dodgy_time));
            BurnChanceField.setText(String.valueOf(b.burn_chance));
            BurnTimeField.setText(String.valueOf(b.burn_time));
            FreezeChanceField.setText(String.valueOf(b.freeze_chance));
            FreezeTimeField.setText(String.valueOf(b.freeze_time));
            SizeChangeChanceField.setText(String.valueOf(b.size_change_chance));
            SizeChangeTimeField.setText(String.valueOf(b.size_change_time));
            SizeChangeStrengthField.setText(String.valueOf(b.size_change_strength));
            AtkChangeChanceField.setText(String.valueOf(b.atk_change_chance));
            AtkChangeTimeField.setText(String.valueOf(b.atk_change_time));
            AtkChangeStrengthField.setText(String.valueOf(b.atk_change_strength));
            DefChangeChanceField.setText(String.valueOf(b.def_change_chance));
            DefChangeTimeField.setText(String.valueOf(b.def_change_time));
            DefChangeStrengthField.setText(String.valueOf(b.def_change_strength));
            AllergicChanceField.setText(String.valueOf(b.allergic_chance));
            AllergicTimeField.setText(String.valueOf(b.allergic_time));
            OHKOChanceField.setText(String.valueOf(b.ohko_chance));
            ChargeStrengthField.setText(String.valueOf(b.charge_strength));
            FastChanceField.setText(String.valueOf(b.fast_chance));
            FastTimeField.setText(String.valueOf(b.fast_time));
            SlowChanceField.setText(String.valueOf(b.slow_chance));
            SlowTimeField.setText(String.valueOf(b.slow_time));
            FrightChanceField.setText(String.valueOf(b.fright_chance));
            GaleForceChanceField.setText(String.valueOf(b.gale_force_chance));
            PaybackTimeField.setText(String.valueOf(b.payback_time));
            HoldFastTimeField.setText(String.valueOf(b.hold_fast_time));
            InvisibleChanceField.setText(String.valueOf(b.invisible_chance));
            InvisibleTimeField.setText(String.valueOf(b.invisible_time));
            HPRegenTimeField.setText(String.valueOf(b.hp_regen_time));
            HPRegenStrengthField.setText(String.valueOf(b.hp_regen_strength));
            FPRegenTimeField.setText(String.valueOf(b.fp_regen_time));
            FPRegenStrengthField.setText(String.valueOf(b.fp_regen_strength));
            STGFall1Field.setText(String.valueOf(b.stage_background_fallweight1));
            STGFall2Field.setText(String.valueOf(b.stage_background_fallweight2));
            STGFall3Field.setText(String.valueOf(b.stage_background_fallweight3));
            STGFall4Field.setText(String.valueOf(b.stage_background_fallweight4));
            STGNozzleTurnField.setText(String.valueOf(b.stage_nozzle_turn_chance));
            STGNozzleFireField.setText(String.valueOf(b.stage_nozzle_fire_chance));
            STGCeilingFallField.setText(String.valueOf(b.stage_ceiling_fall_chance));
            STGObjectFallField.setText(String.valueOf(b.stage_object_fall_chance));
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
        else if (selected instanceof BattleWeapon b)
        {
            b.accuracy = Integer.parseInt(AccuracyField.getText());
            b.fp_cost = Integer.parseInt(FPCostField.getText());
            b.sp_cost = Integer.parseInt(SPCostField.getText());
            b.superguard_state = SuperguardStateSelector.getSelectionModel().getSelectedIndex();
            b.sylish_multiplier = Integer.parseInt(StylishMultiplierField.getText());
            b.bingo_slot_inc_chance = Integer.parseInt(BingoSlotIncChanceField.getText());
            b.base_damage1 = Integer.parseInt(BaseDamage1Field.getText());
            b.base_damage2 = Integer.parseInt(BaseDamage2Field.getText());
            b.base_damage3 = Integer.parseInt(BaseDamage3Field.getText());
            b.base_damage4 = Integer.parseInt(BaseDamage4Field.getText());
            b.base_damage5 = Integer.parseInt(BaseDamage5Field.getText());
            b.base_damage6 = Integer.parseInt(BaseDamage6Field.getText());
            b.base_damage7 = Integer.parseInt(BaseDamage7Field.getText());
            b.base_damage8 = Integer.parseInt(BaseDamage8Field.getText());
            b.base_fpdamage1 = Integer.parseInt(BaseFPDamage1Field.getText());
            b.base_fpdamage2 = Integer.parseInt(BaseFPDamage2Field.getText());
            b.base_fpdamage3 = Integer.parseInt(BaseFPDamage3Field.getText());
            b.base_fpdamage4 = Integer.parseInt(BaseFPDamage4Field.getText());
            b.base_fpdamage5 = Integer.parseInt(BaseFPDamage5Field.getText());
            b.base_fpdamage6 = Integer.parseInt(BaseFPDamage6Field.getText());
            b.base_fpdamage7 = Integer.parseInt(BaseFPDamage7Field.getText());
            b.base_fpdamage8 = Integer.parseInt(BaseFPDamage8Field.getText());
            b.CannotTargetMarioOrShellShield = TCF1Box.isSelected();
            b.CannotTargetPartner = TCF2Box.isSelected();
            b.CannotTargetEnemy = TCF3Box.isSelected();
            b.CannotTargetOppositeAlliance = TCF4Box.isSelected();
            b.CannotTargetOwnAlliance = TCF5Box.isSelected();
            b.CannotTargetSelf = TCF6Box.isSelected();
            b.CannotTargetSameSpecies = TCF7Box.isSelected();
            b.OnlyTargetSelf = TCF8Box.isSelected();
            b.OnlyTargetMario = TCF9Box.isSelected();
            b.SingleTarget = TCF10Box.isSelected();
            b.MultipleTarget = TCF11Box.isSelected();
            b.Tattlelike = TPF1Box.isSelected();
            b.CannotTargetCeiling = TPF2Box.isSelected();
            b.CannotTargetFloating = TPF3Box.isSelected();
            b.CannotTargetGrounded = TPF4Box.isSelected();
            b.Jumplike = TPF5Box.isSelected();
            b.Hammerlike = TPF6Box.isSelected();
            b.ShellTosslike = TPF7Box.isSelected();
            b.RecoilDamage = TPF8Box.isSelected();
            b.CanOnlyTargetFrontmost = TPF9Box.isSelected();
            b.TargetSameAllianceDirection = TPF10Box.isSelected();
            b.TargetOppositeAllianceDirection = TPF11Box.isSelected();
            b.element = ElementSelector.getSelectionModel().getSelectedIndex();
            b.damage_pattern = IndexToDP(DamagePatternSelector.getSelectionModel().getSelectedIndex());
            b.BadgeCanAffectPower = SPF1Box.isSelected();
            b.StatusCanAffectPower = SPF2Box.isSelected();
            b.IsChargeable = SPF3Box.isSelected();
            b.CannotMiss = SPF4Box.isSelected();
            b.DiminishingReturnsByHit = SPF5Box.isSelected();
            b.DiminishingReturnsByTarget = SPF6Box.isSelected();
            b.PiercesDefense = SPF7Box.isSelected();
            b.IgnoreTargetStatusVulnerability = SPF8Box.isSelected();
            b.IgnitesIfBurned = SPF9Box.isSelected();
            b.FlipsShellEnemies = SPF10Box.isSelected();
            b.FlipsBombFlippableEnemies = SPF11Box.isSelected();
            b.GroundsWingedEnemies = SPF12Box.isSelected();
            b.CanBeUsedAsConfusedAction = SPF13Box.isSelected();
            b.Unguardable = SPF14Box.isSelected();
            b.Electric = CRF1Box.isSelected();
            b.TopSpiky = CRF2Box.isSelected();
            b.PreemptiveFrontSpiky = CRF3Box.isSelected();
            b.FrontSpiky = CRF4Box.isSelected();
            b.Fiery = CRF5Box.isSelected();
            b.Icy = CRF6Box.isSelected();
            b.Poison = CRF7Box.isSelected();
            b.Explosive = CRF8Box.isSelected();
            b.VolatileExplosive = CRF9Box.isSelected();
            b.Payback = CRF10Box.isSelected();
            b.HoldFast = CRF11Box.isSelected();
            b.PreferMario = TWF1Box.isSelected();
            b.PreferPartner = TWF2Box.isSelected();
            b.PreferFront = TWF3Box.isSelected();
            b.PreferBack = TWF4Box.isSelected();
            b.PreferSameAlliance = TWF5Box.isSelected();
            b.PreferOppositeAlliance = TWF6Box.isSelected();
            b.PreferLessHealthy = TWF7Box.isSelected();
            b.GreatlyPreferLessHealthy = TWF8Box.isSelected();
            b.PreferLowerHP = TWF9Box.isSelected();
            b.PreferHigherHP = TWF10Box.isSelected();
            b.PreferInPeril = TWF11Box.isSelected();
            b.ChooseWeightedRandomly = TWF12Box.isSelected();
            b.sleep_chance = Integer.parseInt(SleepChanceField.getText());
            b.sleep_time = Integer.parseInt(SleepTimeField.getText());
            b.stop_chance = Integer.parseInt(StopChanceField.getText());
            b.stop_time = Integer.parseInt(StopTimeField.getText());
            b.dizzy_chance = Integer.parseInt(DizzyChanceField.getText());
            b.dizzy_time = Integer.parseInt(DizzyTimeField.getText());
            b.poison_chance = Integer.parseInt(PoisonChanceField.getText());
            b.poison_time = Integer.parseInt(PoisonTimeField.getText());
            b.poison_strength = Integer.parseInt(PoisonStrengthField.getText());
            b.confuse_chance = Integer.parseInt(ConfuseChanceField.getText());
            b.confuse_time = Integer.parseInt(ConfuseTimeField.getText());
            b.electric_chance = Integer.parseInt(ElectricChanceField.getText());
            b.electric_time = Integer.parseInt(ElectricTimeField.getText());
            b.dodgy_chance = Integer.parseInt(DodgyChanceField.getText());
            b.dodgy_time = Integer.parseInt(DodgyTimeField.getText());
            b.burn_chance = Integer.parseInt(BurnChanceField.getText());
            b.burn_time = Integer.parseInt(BurnTimeField.getText());
            b.freeze_chance = Integer.parseInt(FreezeChanceField.getText());
            b.freeze_time = Integer.parseInt(FreezeTimeField.getText());
            b.size_change_chance = Integer.parseInt(SizeChangeChanceField.getText());
            b.size_change_time = Integer.parseInt(SizeChangeTimeField.getText());
            b.size_change_strength = Integer.parseInt(SizeChangeStrengthField.getText());
            b.atk_change_chance = Integer.parseInt(AtkChangeChanceField.getText());
            b.atk_change_time = Integer.parseInt(AtkChangeTimeField.getText());
            b.atk_change_strength = Integer.parseInt(AtkChangeStrengthField.getText());
            b.def_change_chance = Integer.parseInt(DefChangeChanceField.getText());
            b.def_change_time = Integer.parseInt(DefChangeTimeField.getText());
            b.def_change_strength = Integer.parseInt(DefChangeStrengthField.getText());
            b.allergic_chance = Integer.parseInt(AllergicChanceField.getText());
            b.allergic_time = Integer.parseInt(AllergicTimeField.getText());
            b.ohko_chance = Integer.parseInt(OHKOChanceField.getText());
            b.charge_strength = Integer.parseInt(ChargeStrengthField.getText());
            b.fast_chance = Integer.parseInt(FastChanceField.getText());
            b.fast_time = Integer.parseInt(FastTimeField.getText());
            b.slow_chance = Integer.parseInt(SlowChanceField.getText());
            b.slow_time = Integer.parseInt(SlowTimeField.getText());
            b.fright_chance = Integer.parseInt(FrightChanceField.getText());
            b.gale_force_chance = Integer.parseInt(GaleForceChanceField.getText());
            b.payback_time = Integer.parseInt(PaybackTimeField.getText());
            b.hold_fast_time = Integer.parseInt(HoldFastTimeField.getText());
            b.invisible_chance = Integer.parseInt(InvisibleChanceField.getText());
            b.invisible_time = Integer.parseInt(InvisibleTimeField.getText());
            b.hp_regen_time = Integer.parseInt(HPRegenTimeField.getText());
            b.hp_regen_strength = Integer.parseInt(HPRegenStrengthField.getText());
            b.fp_regen_time = Integer.parseInt(FPRegenTimeField.getText());
            b.fp_regen_strength = Integer.parseInt(FPRegenStrengthField.getText());
            b.stage_background_fallweight1 = Integer.parseInt(STGFall1Field.getText());
            b.stage_background_fallweight2 = Integer.parseInt(STGFall2Field.getText());
            b.stage_background_fallweight3 = Integer.parseInt(STGFall3Field.getText());
            b.stage_background_fallweight4 = Integer.parseInt(STGFall4Field.getText());
            b.stage_nozzle_turn_chance = Integer.parseInt(STGNozzleTurnField.getText());
            b.stage_nozzle_fire_chance = Integer.parseInt(STGNozzleFireField.getText());
            b.stage_ceiling_fall_chance = Integer.parseInt(STGCeilingFallField.getText());
            b.stage_object_fall_chance = Integer.parseInt(STGObjectFallField.getText());
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Damage Pattern into it's index
     */
    public static int DPtoIndex(int DP)
    {
        switch(DP)
        {
            case 0: return 0;
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 8: return 4;
            case 10: return 5;
            case 11: return 6;
            case 12: return 7;
            case 13: return 8;
            case 14: return 9;
            case 16: return 10;
            case 19: return 11;
            case 20: return 12;
            case 21: return 13;
            case 22: return 14;
            case 23: return 15;
            case 24: return 16;
            case 25: return 17;
            case 27: return 18;
            default: return 0;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a index into it's Damage Pattern
     */
    public static int IndexToDP(int index)
    {
        switch(index)
        {
            case 0: return 0;
            case 1: return 4;
            case 2: return 5;
            case 3: return 6;
            case 4: return 8;
            case 5: return 10;
            case 6: return 11;
            case 7: return 12;
            case 8: return 13;
            case 9: return 14;
            case 10: return 16;
            case 11: return 19;
            case 12: return 20;
            case 13: return 21;
            case 14: return 22;
            case 15: return 23;
            case 16: return 24;
            case 17: return 25;
            case 18: return 27;
            default: return 0;
        }
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}