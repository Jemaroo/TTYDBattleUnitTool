import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.awt.Desktop;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.technicjelle.UpdateChecker;

//TODO
// - Implement Starting Health/Health Upgrades?
// - Enemy Formations?

public class GUI extends Application 
{
    private ArrayList<String> validFileNames = new ArrayList<String>();
    boolean isFileOpened = false;
    Stage window;
    File givenFile;
    private ArrayList<UnitData> units = new ArrayList<UnitData>();
    ComboBox<UnitData> unitSelector = null;
    private ListView<Object> kindList = null;
    private ListView<Object> defenseList = null;
    private ListView<StatusVulnerability> statuslist = null;
    private ListView<BattleWeapon> weaponList = null;
    Button saveChangesButton = new Button("Save Struct Changes");
    Button saveStructPresetButton = new Button("Save Struct Preset");
    Button loadStructPresetButton = new Button("Load Struct Preset");
    HBox upperBox = new HBox();
    HBox upperBox2 = new HBox();

    Image unitImage = new Image(getClass().getResource("/icons/unit.png").toExternalForm());
    Image defendPlusImage = new Image(getClass().getResource("/icons/defendPlus.png").toExternalForm());
    Image feelingFineImage = new Image(getClass().getResource("/icons/feelingFine.png").toExternalForm());
    Image hammerImage = new Image(getClass().getResource("/icons/hammer.png").toExternalForm());
    Image heartImage = new Image(getClass().getResource("/icons/heart.png").toExternalForm());
    Image dangerHeartCustomImage = new Image(getClass().getResource("/icons/dangerHeartCustom.png").toExternalForm());
    Image audienceStarImage = new Image(getClass().getResource("/icons/audienceStar.png").toExternalForm());
    Image XPImage = new Image(getClass().getResource("/icons/XP.png").toExternalForm());
    Image coinImage = new Image(getClass().getResource("/icons/coin.png").toExternalForm());
    Image runArrowImage = new Image(getClass().getResource("/icons/runArrow.png").toExternalForm());
    Image powerBounceImage = new Image(getClass().getResource("/icons/powerBounce.png").toExternalForm());
    Image ultraHammerImage = new Image(getClass().getResource("/icons/ultraHammer.png").toExternalForm());
    Image defenseUpStatusImage = new Image(getClass().getResource("/icons/defenseUpStatus.png").toExternalForm());
    Image spikeShieldCustomImage = new Image(getClass().getResource("/icons/spikeShieldCustom.png").toExternalForm());
    Image fireShieldCustomImage = new Image(getClass().getResource("/icons/fireShieldCustom.png").toExternalForm());
    Image iceShieldCustomImage = new Image(getClass().getResource("/icons/iceShieldCustom.png").toExternalForm());
    Image burstShieldCustomImage = new Image(getClass().getResource("/icons/burstShieldCustom.png").toExternalForm());
    Image zapShieldCustomImage = new Image(getClass().getResource("/icons/zapShieldCustom.png").toExternalForm());
    Image sleepStatusImage = new Image(getClass().getResource("/icons/sleepStatus.png").toExternalForm());
    Image stopStatusImage = new Image(getClass().getResource("/icons/stopStatus.png").toExternalForm());
    Image dizzyStatusImage = new Image(getClass().getResource("/icons/dizzyStatus.png").toExternalForm());
    Image poisonStatusImage = new Image(getClass().getResource("/icons/poisonStatus.png").toExternalForm());
    Image confuseStatusImage = new Image(getClass().getResource("/icons/confuseStatus.png").toExternalForm());
    Image electricStatusImage = new Image(getClass().getResource("/icons/electricStatus.png").toExternalForm());
    Image burnStatusImage = new Image(getClass().getResource("/icons/burnStatus.png").toExternalForm());
    Image freezeStatusImage = new Image(getClass().getResource("/icons/freezeStatus.png").toExternalForm());
    Image hugeStatusImage = new Image(getClass().getResource("/icons/hugeStatus.png").toExternalForm());
    Image tinyStatusImage = new Image(getClass().getResource("/icons/tinyStatus.png").toExternalForm());
    Image attackUpStatusImage = new Image(getClass().getResource("/icons/attackUpStatus.png").toExternalForm());
    Image attackDownStatusImage = new Image(getClass().getResource("/icons/attackDownStatus.png").toExternalForm());
    Image defenseDownStatusImage = new Image(getClass().getResource("/icons/defenseDownStatus.png").toExternalForm());
    Image allergicStatusImage = new Image(getClass().getResource("/icons/allergicStatus.png").toExternalForm());
    Image frightMaskImage = new Image(getClass().getResource("/icons/frightMask.png").toExternalForm());
    Image flurriePartnerSwitchImage = new Image(getClass().getResource("/icons/flurriePartnerSwitch.png").toExternalForm());
    Image fastStatusImage = new Image(getClass().getResource("/icons/fastStatus.png").toExternalForm());
    Image slowStatusImage = new Image(getClass().getResource("/icons/slowStatus.png").toExternalForm());
    Image dodgyStatusImage = new Image(getClass().getResource("/icons/dodgyStatus.png").toExternalForm());
    Image invisibleStatusImage = new Image(getClass().getResource("/icons/invisibleStatus.png").toExternalForm());
    Image garnetStarImage = new Image(getClass().getResource("/icons/garnetStar.png").toExternalForm());
    Image appealActionImage = new Image(getClass().getResource("/icons/appealAction.png").toExternalForm());
    Image flowerImage = new Image(getClass().getResource("/icons/flower.png").toExternalForm());
    Image SPOrb1Image = new Image(getClass().getResource("/icons/SPOrb1.png").toExternalForm());
    Image superChargeImage = new Image(getClass().getResource("/icons/superCharge.png").toExternalForm());
    Image shineSpriteImage = new Image(getClass().getResource("/icons/shineSprite.png").toExternalForm());
    Image zapTapImage = new Image(getClass().getResource("/icons/zapTap.png").toExternalForm());
    Image tacticsFlagImage = new Image(getClass().getResource("/icons/tacticsFlag.png").toExternalForm());
    Image chargeImage = new Image(getClass().getResource("/icons/charge.png").toExternalForm());
    Image paybackStatus = new Image(getClass().getResource("/icons/paybackStatus.png").toExternalForm());
    Image bobberyPartnerSwitchImage = new Image(getClass().getResource("/icons/bobberyPartnerSwitch.png").toExternalForm());
    Image HPRegenStatusImage = new Image(getClass().getResource("/icons/HPRegenStatus.png").toExternalForm());
    Image FPRegenStatusImage = new Image(getClass().getResource("/icons/FPRegenStatus.png").toExternalForm());
    Image gateHandleImage = new Image(getClass().getResource("/icons/gateHandle.png").toExternalForm());
    Image actionCommandStarImage = new Image(getClass().getResource("/icons/actionCommandStar.png").toExternalForm());
    Image unknownImage = new Image(getClass().getResource("/icons/unknown.png").toExternalForm());
    Image unusedPaperImage = new Image(getClass().getResource("/icons/unusedPaper.png").toExternalForm());
    Image magicalMap2Image = new Image(getClass().getResource("/icons/magicalMap2.png").toExternalForm());
    Image fahrOutpostBombCustomImage = new Image(getClass().getResource("/icons/fahrOutpostBombCustom.png").toExternalForm());
    Image puniCustomImage = new Image(getClass().getResource("/icons/puniCustom.png").toExternalForm());
    Image koopaCustomImage = new Image(getClass().getResource("/icons/koopaCustom.png").toExternalForm());

    Image unitAmazyDayzeeImage = new Image(getClass().getResource("/icons/unitAmazyDayzee.png").toExternalForm());
    Image unitArantulaImage = new Image(getClass().getResource("/icons/unitArantula.png").toExternalForm());
    Image unitAtomicBooImage = new Image(getClass().getResource("/icons/unitAtomicBoo.png").toExternalForm());
    Image unitBadgeBanditImage = new Image(getClass().getResource("/icons/unitBadgeBandit.png").toExternalForm());
    Image unitBaldCleftImage = new Image(getClass().getResource("/icons/unitBaldCleft.png").toExternalForm());
    Image unitBanditImage = new Image(getClass().getResource("/icons/unitBandit.png").toExternalForm());
    Image unitBeldamImage = new Image(getClass().getResource("/icons/unitBeldam.png").toExternalForm());
    Image unitBigBanditImage = new Image(getClass().getResource("/icons/unitBigBandit.png").toExternalForm());
    Image unitBillBlasterImage = new Image(getClass().getResource("/icons/unitBillBlaster.png").toExternalForm());
    Image unitBlooperImage = new Image(getClass().getResource("/icons/unitBlooper.png").toExternalForm());
    Image unitBobombImage = new Image(getClass().getResource("/icons/unitBob-omb.png").toExternalForm());
    Image unitBobulkImage = new Image(getClass().getResource("/icons/unitBob-ulk.png").toExternalForm());
    Image unitBombshellBillImage = new Image(getClass().getResource("/icons/unitBombshellBill.png").toExternalForm());
    Image unitBombshellBillBlasterImage = new Image(getClass().getResource("/icons/unitBombshellBillBlaster.png").toExternalForm());
    Image unitBombSquadBombImage = new Image(getClass().getResource("/icons/unitBombSquadBomb.png").toExternalForm());
    Image unitBonetailImage = new Image(getClass().getResource("/icons/unitBonetail.png").toExternalForm());
    Image unitBooImage = new Image(getClass().getResource("/icons/unitBoo.png").toExternalForm());
    Image unitBoomerangBroImage = new Image(getClass().getResource("/icons/unitBoomerangBro.png").toExternalForm());
    Image unitBowserImage = new Image(getClass().getResource("/icons/unitBowser.png").toExternalForm());
    Image unitBristleImage = new Image(getClass().getResource("/icons/unitBristle.png").toExternalForm());
    Image unitBulkyBobombImage = new Image(getClass().getResource("/icons/unitBulkyBob-omb.png").toExternalForm());
    Image unitBulletBillImage = new Image(getClass().getResource("/icons/unitBulletBill.png").toExternalForm());
    Image unitBuzzyBeetleImage = new Image(getClass().getResource("/icons/unitBuzzyBeetle.png").toExternalForm());
    Image unitChainChompImage = new Image(getClass().getResource("/icons/unitChainChomp.png").toExternalForm());
    Image unitCleftImage = new Image(getClass().getResource("/icons/unitCleft.png").toExternalForm());
    Image unitCortezImage = new Image(getClass().getResource("/icons/unitCortez.png").toExternalForm());
    Image unitCortezBonePileImage = new Image(getClass().getResource("/icons/unitCortezBonePile.png").toExternalForm());
    Image unitCortezHookImage = new Image(getClass().getResource("/icons/unitCortezHook.png").toExternalForm());
    Image unitCortezRapierImage = new Image(getClass().getResource("/icons/unitCortezRapier.png").toExternalForm());
    Image unitCortezSaberImage = new Image(getClass().getResource("/icons/unitCortezSaber.png").toExternalForm());
    Image unitCortezSwordImage = new Image(getClass().getResource("/icons/unitCortezSword.png").toExternalForm());
    Image unitCrazeeDayzeeImage = new Image(getClass().getResource("/icons/unitCrazeeDayzee.png").toExternalForm());
    Image unitDarkBonesImage = new Image(getClass().getResource("/icons/unitDarkBones.png").toExternalForm());
    Image unitDarkBooImage = new Image(getClass().getResource("/icons/unitDarkBoo.png").toExternalForm());
    Image unitDarkBristleImage = new Image(getClass().getResource("/icons/unitDarkBristle.png").toExternalForm());
    Image unitDarkCrawImage = new Image(getClass().getResource("/icons/unitDarkCraw.png").toExternalForm());
    Image unitDarkKoopaImage = new Image(getClass().getResource("/icons/unitDarkKoopa.png").toExternalForm());
    Image unitDarkKoopatrolImage = new Image(getClass().getResource("/icons/unitDarkKoopatrol.png").toExternalForm());
    Image unitDarkLakituImage = new Image(getClass().getResource("/icons/unitDarkLakitu.png").toExternalForm());
    Image unitDarkParatroopaImage = new Image(getClass().getResource("/icons/unitDarkParatroopa.png").toExternalForm());
    Image unitDarkPuffImage = new Image(getClass().getResource("/icons/unitDarkPuff.png").toExternalForm());
    Image unitDarkWizzerdImage = new Image(getClass().getResource("/icons/unitDarkWizzerd.png").toExternalForm());
    Image unitDooplissImage = new Image(getClass().getResource("/icons/unitDoopliss.png").toExternalForm());
    Image unitDooplissMarioImage = new Image(getClass().getResource("/icons/unitDooplissMario.png").toExternalForm());
    Image unitDryBonesImage = new Image(getClass().getResource("/icons/unitDryBones.png").toExternalForm());
    Image unitDullBonesImage = new Image(getClass().getResource("/icons/unitDullBones.png").toExternalForm());
    Image unitEliteWizzerdImage = new Image(getClass().getResource("/icons/unitEliteWizzerd.png").toExternalForm());
    Image unitEliteXNautImage = new Image(getClass().getResource("/icons/unitEliteX-Naut.png").toExternalForm());
    Image unitEmberImage = new Image(getClass().getResource("/icons/unitEmber.png").toExternalForm());
    Image unitFireBroImage = new Image(getClass().getResource("/icons/unitFireBro.png").toExternalForm());
    Image unitFlowerFuzzyImage = new Image(getClass().getResource("/icons/unitFlowerFuzzy.png").toExternalForm());
    Image unitFrostPiranhaImage = new Image(getClass().getResource("/icons/unitFrostPiranha.png").toExternalForm());
    Image unitFuzzyImage = new Image(getClass().getResource("/icons/unitFuzzy.png").toExternalForm());
    Image unitGloombaImage = new Image(getClass().getResource("/icons/unitGloomba.png").toExternalForm());
    Image unitGloomtailImage = new Image(getClass().getResource("/icons/unitGloomtail.png").toExternalForm());
    Image unitGoldFuzzyImage = new Image(getClass().getResource("/icons/unitGoldFuzzy.png").toExternalForm());
    Image unitGoombaImage = new Image(getClass().getResource("/icons/unitGoomba.png").toExternalForm());
    Image unitGreenFuzzyImage = new Image(getClass().getResource("/icons/unitGreenFuzzy.png").toExternalForm());
    Image unitGreenMagikoopaImage = new Image(getClass().getResource("/icons/unitGreenMagikoopa.png").toExternalForm());
    Image unitGrodusImage = new Image(getClass().getResource("/icons/unitGrodus.png").toExternalForm());
    Image unitGrodusXImage = new Image(getClass().getResource("/icons/unitGrodusX.png").toExternalForm());
    Image unitGusImage = new Image(getClass().getResource("/icons/unitGus.png").toExternalForm());
    Image unitHammerBroImage = new Image(getClass().getResource("/icons/unitHammerBro.png").toExternalForm());
    Image unitHooktailImage = new Image(getClass().getResource("/icons/unitHooktail.png").toExternalForm());
    Image unitHyperBaldCleftImage = new Image(getClass().getResource("/icons/unitHyperBaldCleft.png").toExternalForm());
    Image unitHyperCleftImage = new Image(getClass().getResource("/icons/unitHyperCleft.png").toExternalForm());
    Image unitHyperGoombaImage = new Image(getClass().getResource("/icons/unitHyperGoomba.png").toExternalForm());
    Image unitHyperParagoombaImage = new Image(getClass().getResource("/icons/unitHyperParagoomba.png").toExternalForm());
    Image unitHyperSpikyGoombaImage = new Image(getClass().getResource("/icons/unitHyperSpikyGoomba.png").toExternalForm());
    Image unitIcePuffImage = new Image(getClass().getResource("/icons/unitIcePuff.png").toExternalForm());
    Image unitIronCleft1Image = new Image(getClass().getResource("/icons/unitIronCleft1.png").toExternalForm());
    Image unitIronCleft2Image = new Image(getClass().getResource("/icons/unitIronCleft2.png").toExternalForm());
    Image unitKammyKoopaImage = new Image(getClass().getResource("/icons/unitKammyKoopa.png").toExternalForm());
    Image unitKoopatrolImage = new Image(getClass().getResource("/icons/unitKoopatrol.png").toExternalForm());
    Image unitKoopaTroopaImage = new Image(getClass().getResource("/icons/unitKoopaTroopa.png").toExternalForm());
    Image unitKPKoopaImage = new Image(getClass().getResource("/icons/unitKPKoopa.png").toExternalForm());
    Image unitKPParatroopaImage = new Image(getClass().getResource("/icons/unitKPParatroopa.png").toExternalForm());
    Image unitLakituImage = new Image(getClass().getResource("/icons/unitLakitu.png").toExternalForm());
    Image unitLavaBubbleImage = new Image(getClass().getResource("/icons/unitLavaBubble.png").toExternalForm());
    Image unitLordCrumpImage = new Image(getClass().getResource("/icons/unitLordCrump.png").toExternalForm());
    Image unitMachoGrubbaImage = new Image(getClass().getResource("/icons/unitMachoGrubba.png").toExternalForm());
    Image unitMagikoopaImage = new Image(getClass().getResource("/icons/unitMagikoopa.png").toExternalForm());
    Image unitMagnusVonGrappleImage = new Image(getClass().getResource("/icons/unitMagnusVonGrapple.png").toExternalForm());
    Image unitMagnusVonGrapple2Image = new Image(getClass().getResource("/icons/unitMagnusVonGrapple2.png").toExternalForm());
    Image unitMarilynImage = new Image(getClass().getResource("/icons/unitMarilyn.png").toExternalForm());
    Image unitMiniXYuxImage = new Image(getClass().getResource("/icons/unitMini-X-Yux.png").toExternalForm());
    Image unitMiniYuxImage = new Image(getClass().getResource("/icons/unitMini-Yux.png").toExternalForm());
    Image unitMiniZYuxImage = new Image(getClass().getResource("/icons/unitMini-Z-Yux.png").toExternalForm());
    Image unitMoonCleftImage = new Image(getClass().getResource("/icons/unitMoonCleft.png").toExternalForm());
    Image unitPalePiranhaImage = new Image(getClass().getResource("/icons/unitPalePiranha.png").toExternalForm());
    Image unitParabuzzyImage = new Image(getClass().getResource("/icons/unitParabuzzy.png").toExternalForm());
    Image unitParagloombaImage = new Image(getClass().getResource("/icons/unitParagloomba.png").toExternalForm());
    Image unitParagoombaImage = new Image(getClass().getResource("/icons/unitParagoomba.png").toExternalForm());
    Image unitParatroopaImage = new Image(getClass().getResource("/icons/unitParakoopa.png").toExternalForm());
    Image unitPhantomEmberImage = new Image(getClass().getResource("/icons/unitPhantomEmber.png").toExternalForm());
    Image unitPiderImage = new Image(getClass().getResource("/icons/unitPider.png").toExternalForm());
    Image unitPiranhaPlantImage = new Image(getClass().getResource("/icons/unitPiranhaPlant.png").toExternalForm());
    Image unitPoisonPokeyImage = new Image(getClass().getResource("/icons/unitPoisonPokey.png").toExternalForm());
    Image unitPoisonPuffImage = new Image(getClass().getResource("/icons/unitPoisonPuff.png").toExternalForm());
    Image unitPokeyImage = new Image(getClass().getResource("/icons/unitPokey.png").toExternalForm());
    Image unitProfessorFranklyImage = new Image(getClass().getResource("/icons/unitProfessorFrankly.png").toExternalForm());
    Image unitPutridPiranhaImage = new Image(getClass().getResource("/icons/unitPutridPiranha.png").toExternalForm());
    Image unitRawkHawkImage = new Image(getClass().getResource("/icons/unitRawkHawk.png").toExternalForm());
    Image unitRedBonesImage = new Image(getClass().getResource("/icons/unitRedBones.png").toExternalForm());
    Image unitRedChompImage = new Image(getClass().getResource("/icons/unitRedChomp.png").toExternalForm());
    Image unitRedMagikoopaImage = new Image(getClass().getResource("/icons/unitRedMagikoopa.png").toExternalForm());
    Image unitRedSpikyBuzzyImage = new Image(getClass().getResource("/icons/unitRedSpikyBuzzy.png").toExternalForm());
    Image unitRuffPuffImage = new Image(getClass().getResource("/icons/unitRuffPuff.png").toExternalForm());
    Image unitShadowHand1Image = new Image(getClass().getResource("/icons/unitShadowHand1.png").toExternalForm());
    Image unitShadowHand2Image = new Image(getClass().getResource("/icons/unitShadowHand2.png").toExternalForm());
    Image unitShadowPeachImage = new Image(getClass().getResource("/icons/unitShadowPeach.png").toExternalForm());
    Image unitShadowQueenImage = new Image(getClass().getResource("/icons/unitShadowQueen.png").toExternalForm());
    Image unitShadyKoopaImage = new Image(getClass().getResource("/icons/unitShadyKoopa.png").toExternalForm());
    Image unitShadyParatroopaImage = new Image(getClass().getResource("/icons/unitShadyParatroopa.png").toExternalForm());
    Image unitShellShieldImage = new Image(getClass().getResource("/icons/unitShellShield.png").toExternalForm());
    Image unitSkyBlueSpinyImage = new Image(getClass().getResource("/icons/unitSkyBlueSpiny.png").toExternalForm());
    Image unitSmallShadowHandsImage = new Image(getClass().getResource("/icons/unitSmallShadowHands.png").toExternalForm());
    Image unitSmorgImage = new Image(getClass().getResource("/icons/unitSmorg.png").toExternalForm());
    Image unitSpaniaImage = new Image(getClass().getResource("/icons/unitSpania.png").toExternalForm());
    Image unitSpikyBuzzyImage = new Image(getClass().getResource("/icons/unitSpikyBuzzy.png").toExternalForm());
    Image unitSpikyGloombaImage = new Image(getClass().getResource("/icons/unitSpikyGloomba.png").toExternalForm());
    Image unitSpikyGoombaImage = new Image(getClass().getResource("/icons/unitSpikyGoomba.png").toExternalForm());
    Image unitSpikyParabuzzyImage = new Image(getClass().getResource("/icons/unitSpikyParabuzzy.png").toExternalForm());
    Image unitSpiniaImage = new Image(getClass().getResource("/icons/unitSpinia.png").toExternalForm());
    Image unitSpinyImage = new Image(getClass().getResource("/icons/unitSpiny.png").toExternalForm());
    Image unitSpuniaImage = new Image(getClass().getResource("/icons/unitSpunia.png").toExternalForm());
    Image unitSwampireImage = new Image(getClass().getResource("/icons/unitSwampire.png").toExternalForm());
    Image unitSwooperImage = new Image(getClass().getResource("/icons/unitSwooper.png").toExternalForm());
    Image unitSwoopulaImage = new Image(getClass().getResource("/icons/unitSwoopula.png").toExternalForm());
    Image unitWhiteMagikoopaImage = new Image(getClass().getResource("/icons/unitWhiteMagikoopa.png").toExternalForm());
    Image unitWizzerdImage = new Image(getClass().getResource("/icons/unitWizzerd.png").toExternalForm());
    Image unitXNautImage = new Image(getClass().getResource("/icons/unitX-Naut.png").toExternalForm());
    Image unitXNautPHDImage = new Image(getClass().getResource("/icons/unitX-NautPHD.png").toExternalForm());
    Image unitXYuxImage = new Image(getClass().getResource("/icons/unitX-Yux.png").toExternalForm());
    Image unitYuxImage = new Image(getClass().getResource("/icons/unitYux.png").toExternalForm());
    Image unitZYuxImage = new Image(getClass().getResource("/icons/unitZ-Yux.png").toExternalForm());
    Image femaleToadCustomImage = new Image(getClass().getResource("/icons/femaleToadCustom.png").toExternalForm());
    Image goombellaPartnerSwitchImage = new Image(getClass().getResource("/icons/goombellaPartnerSwitch.png").toExternalForm());
    Image itemsIconImage = new Image(getClass().getResource("/icons/itemsIcon.png").toExternalForm());
    Image koopsPartnerSwitchImage = new Image(getClass().getResource("/icons/koopsPartnerSwitch.png").toExternalForm());
    Image marioHeadCustomImage = new Image(getClass().getResource("/icons/marioHeadCustom.png").toExternalForm());
    Image mowzPartnerSwitchImage = new Image(getClass().getResource("/icons/mowzPartnerSwitch.png").toExternalForm());
    Image toadCustomImage = new Image(getClass().getResource("/icons/toadCustom.png").toExternalForm());
    Image vivianPartnerSwitchImage = new Image(getClass().getResource("/icons/vivianPartnerSwitch.png").toExternalForm());
    Image yoshiPartnerSwitchImage = new Image(getClass().getResource("/icons/yoshiPartnerSwitch.png").toExternalForm());
    
    ImageView kindImageView = new ImageView(unitImage);
    ImageView defenseImageView = new ImageView(defendPlusImage);
    ImageView statusImageView = new ImageView(feelingFineImage);
    ImageView weaponImageView = new ImageView(hammerImage);

    ImageView BUK1ImageView = new ImageView(heartImage);
    ImageView BUK2ImageView = new ImageView(audienceStarImage);
    ImageView BUK3ImageView = new ImageView(XPImage);
    ImageView BUK4ImageView = new ImageView(coinImage);
    ImageView BUK5ImageView = new ImageView(coinImage);
    ImageView BUK6ImageView = new ImageView(coinImage);
    ImageView BUK7ImageView = new ImageView(runArrowImage);
    ImageView BUK8ImageView = new ImageView(powerBounceImage);
    ImageView BUK9ImageView = new ImageView(dangerHeartCustomImage);
    ImageView BUK10ImageView = new ImageView(dangerHeartCustomImage);
    ImageView BUK11ImageView = new ImageView(yoshiPartnerSwitchImage);
    ImageView BUK12ImageView = new ImageView(yoshiPartnerSwitchImage);
    ImageView BUK13ImageView = new ImageView(ultraHammerImage);
    ImageView BUK14ImageView = new ImageView(mowzPartnerSwitchImage);

    ImageView BUD1ImageView = new ImageView(defenseUpStatusImage);
    ImageView BUD2ImageView = new ImageView(defenseUpStatusImage);
    ImageView BUD3ImageView = new ImageView(defenseUpStatusImage);
    ImageView BUD4ImageView = new ImageView(defenseUpStatusImage);
    ImageView BUD5ImageView = new ImageView(defenseUpStatusImage);

    ImageView BUDA1ImageView = new ImageView(spikeShieldCustomImage);
    ImageView BUDA2ImageView = new ImageView(fireShieldCustomImage);
    ImageView BUDA3ImageView = new ImageView(iceShieldCustomImage);
    ImageView BUDA4ImageView = new ImageView(burstShieldCustomImage);
    ImageView BUDA5ImageView = new ImageView(zapShieldCustomImage);

    ImageView SV1ImageView = new ImageView(sleepStatusImage);
    ImageView SV2ImageView = new ImageView(stopStatusImage);
    ImageView SV3ImageView = new ImageView(dizzyStatusImage);
    ImageView SV4ImageView = new ImageView(poisonStatusImage);
    ImageView SV5ImageView = new ImageView(confuseStatusImage);
    ImageView SV6ImageView = new ImageView(electricStatusImage);
    ImageView SV7ImageView = new ImageView(burnStatusImage);
    ImageView SV8ImageView = new ImageView(freezeStatusImage);
    ImageView SV9ImageView = new ImageView(hugeStatusImage);
    ImageView SV10ImageView = new ImageView(tinyStatusImage);
    ImageView SV11ImageView = new ImageView(attackUpStatusImage);
    ImageView SV12ImageView = new ImageView(attackDownStatusImage);
    ImageView SV13ImageView = new ImageView(defenseUpStatusImage);
    ImageView SV14ImageView = new ImageView(defenseDownStatusImage);
    ImageView SV15ImageView = new ImageView(allergicStatusImage);
    ImageView SV16ImageView = new ImageView(frightMaskImage);
    ImageView SV17ImageView = new ImageView(flurriePartnerSwitchImage);
    ImageView SV18ImageView = new ImageView(fastStatusImage);
    ImageView SV19ImageView = new ImageView(slowStatusImage);
    ImageView SV20ImageView = new ImageView(dodgyStatusImage);
    ImageView SV21ImageView = new ImageView(invisibleStatusImage);
    ImageView SV22ImageView = new ImageView(garnetStarImage);

    ImageView BW1ImageView = new ImageView(appealActionImage);
    ImageView BW2ImageView = new ImageView(flowerImage);
    ImageView BW3ImageView = new ImageView(SPOrb1Image);
    ImageView BW4ImageView = new ImageView(superChargeImage);
    ImageView BW5ImageView = new ImageView(audienceStarImage);
    ImageView BW6ImageView = new ImageView(shineSpriteImage);
    ImageView BW7ImageView = new ImageView(hammerImage);
    ImageView BW8ImageView = new ImageView(hammerImage);
    ImageView BW9ImageView = new ImageView(hammerImage);
    ImageView BW10ImageView = new ImageView(hammerImage);
    ImageView BW11ImageView = new ImageView(hammerImage);
    ImageView BW12ImageView = new ImageView(hammerImage);
    ImageView BW13ImageView = new ImageView(hammerImage);
    ImageView BW14ImageView = new ImageView(hammerImage);
    ImageView BW15ImageView = new ImageView(flowerImage);
    ImageView BW16ImageView = new ImageView(flowerImage);
    ImageView BW17ImageView = new ImageView(flowerImage);
    ImageView BW18ImageView = new ImageView(flowerImage);
    ImageView BW19ImageView = new ImageView(flowerImage);
    ImageView BW20ImageView = new ImageView(flowerImage);
    ImageView BW21ImageView = new ImageView(flowerImage);
    ImageView BW22ImageView = new ImageView(flowerImage);
    ImageView BW23ImageView = new ImageView(zapTapImage);
    ImageView BW24ImageView = new ImageView(tacticsFlagImage);
    ImageView BW25ImageView = new ImageView(sleepStatusImage);
    ImageView BW26ImageView = new ImageView(sleepStatusImage);
    ImageView BW27ImageView = new ImageView(stopStatusImage);
    ImageView BW28ImageView = new ImageView(stopStatusImage);
    ImageView BW29ImageView = new ImageView(dizzyStatusImage);
    ImageView BW30ImageView = new ImageView(dizzyStatusImage);
    ImageView BW31ImageView = new ImageView(poisonStatusImage);
    ImageView BW32ImageView = new ImageView(poisonStatusImage);
    ImageView BW33ImageView = new ImageView(poisonStatusImage);
    ImageView BW34ImageView = new ImageView(confuseStatusImage);
    ImageView BW35ImageView = new ImageView(confuseStatusImage);
    ImageView BW36ImageView = new ImageView(electricStatusImage);
    ImageView BW37ImageView = new ImageView(electricStatusImage);
    ImageView BW38ImageView = new ImageView(dodgyStatusImage);
    ImageView BW39ImageView = new ImageView(dodgyStatusImage);
    ImageView BW40ImageView = new ImageView(burnStatusImage);
    ImageView BW41ImageView = new ImageView(burnStatusImage);
    ImageView BW42ImageView = new ImageView(freezeStatusImage);
    ImageView BW43ImageView = new ImageView(freezeStatusImage);
    ImageView BW44ImageView = new ImageView(hugeStatusImage);
    ImageView BW45ImageView = new ImageView(hugeStatusImage);
    ImageView BW46ImageView = new ImageView(hugeStatusImage);
    ImageView BW47ImageView = new ImageView(attackDownStatusImage);
    ImageView BW48ImageView = new ImageView(attackDownStatusImage);
    ImageView BW49ImageView = new ImageView(attackDownStatusImage);
    ImageView BW50ImageView = new ImageView(defenseUpStatusImage);
    ImageView BW51ImageView = new ImageView(defenseUpStatusImage);
    ImageView BW52ImageView = new ImageView(defenseUpStatusImage);
    ImageView BW53ImageView = new ImageView(allergicStatusImage);
    ImageView BW54ImageView = new ImageView(allergicStatusImage);
    ImageView BW55ImageView = new ImageView(garnetStarImage);
    ImageView BW56ImageView = new ImageView(chargeImage);
    ImageView BW57ImageView = new ImageView(fastStatusImage);
    ImageView BW58ImageView = new ImageView(fastStatusImage);
    ImageView BW59ImageView = new ImageView(slowStatusImage);
    ImageView BW60ImageView = new ImageView(slowStatusImage);
    ImageView BW61ImageView = new ImageView(frightMaskImage);
    ImageView BW62ImageView = new ImageView(flurriePartnerSwitchImage);
    ImageView BW63ImageView = new ImageView(paybackStatus);
    ImageView BW64ImageView = new ImageView(bobberyPartnerSwitchImage);
    ImageView BW65ImageView = new ImageView(invisibleStatusImage);
    ImageView BW66ImageView = new ImageView(invisibleStatusImage);
    ImageView BW67ImageView = new ImageView(HPRegenStatusImage);
    ImageView BW68ImageView = new ImageView(HPRegenStatusImage);
    ImageView BW69ImageView = new ImageView(FPRegenStatusImage);
    ImageView BW70ImageView = new ImageView(FPRegenStatusImage);
    ImageView BW71ImageView = new ImageView(gateHandleImage);
    ImageView BW72ImageView = new ImageView(gateHandleImage);
    ImageView BW73ImageView = new ImageView(gateHandleImage);
    ImageView BW74ImageView = new ImageView(gateHandleImage);
    ImageView BW75ImageView = new ImageView(gateHandleImage);
    ImageView BW76ImageView = new ImageView(gateHandleImage);
    ImageView BW77ImageView = new ImageView(gateHandleImage);
    ImageView BW78ImageView = new ImageView(gateHandleImage);
    ImageView BW79ImageView = new ImageView(actionCommandStarImage);

    private TextField HPField = new TextField();
    private TextField dangerHPField = new TextField();
    private TextField perilHPField = new TextField();
    private TextField levelField = new TextField();
    private TextField bonusXPField = new TextField();
    private TextField bonusCoinField = new TextField();
    private TextField bonusCoinRateField = new TextField();
    private TextField baseCoinField = new TextField();
    private TextField runRateField = new TextField();
    private TextField pbCapField = new TextField();
    private TextField swallowChanceField = new TextField();
    ComboBox<String> swallowAttributeSelector = new ComboBox<>();
    private TextField UltraHammerKnockChanceField = new TextField();
    private TextField ItemStealParameterField = new TextField();

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
    ComboBox<String> ACLevelSelector = new ComboBox<>();
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

    private CheckBox PAF1Box = new CheckBox();
    private CheckBox PAF2Box = new CheckBox();
    private CheckBox PAF3Box = new CheckBox();
    private CheckBox PAF4Box = new CheckBox();
    private CheckBox PAF5Box = new CheckBox();
    private CheckBox PAF6Box = new CheckBox();
    private CheckBox PAF7Box = new CheckBox();
    private CheckBox PAF8Box = new CheckBox();
    private CheckBox PAF9Box = new CheckBox();
    private CheckBox PAF10Box = new CheckBox();
    private CheckBox PAF11Box = new CheckBox();
    private CheckBox PAF12Box = new CheckBox();
    private CheckBox PAF13Box = new CheckBox();
    private CheckBox PAF14Box = new CheckBox();
    private CheckBox PAF15Box = new CheckBox();
    private CheckBox PAF16Box = new CheckBox();
    private CheckBox PCAF1Box = new CheckBox();
    private CheckBox PCAF2Box = new CheckBox();
    private CheckBox PCAF3Box = new CheckBox();
    private CheckBox PCAF4Box = new CheckBox();
    private CheckBox PCAF5Box = new CheckBox();
    private CheckBox PCAF6Box = new CheckBox();
    private CheckBox PCAF7Box = new CheckBox();
    private CheckBox PCAF8Box = new CheckBox();
    private CheckBox PCAF9Box = new CheckBox();
    private CheckBox PCAF10Box = new CheckBox();
    private CheckBox PCAF11Box = new CheckBox();
    private CheckBox PCAF12Box = new CheckBox();
    private CheckBox PCAF13Box = new CheckBox();

    @Override
    public void start(Stage primaryStage) 
    {
        String[] tempValidFileNames = {"main.dol", "Start.dol", "aji.rel", "bom.rel", "dou.rel", "eki.rel", "gon.rel", "gor.rel", "gra.rel", "hei.rel", "jin.rel", "jon.rel", "las.rel", "moo.rel", "mri.rel", "muj.rel", "nok.rel", "pik.rel", "rsh.rel", "tik.rel", "tou2.rel", "win.rel", "mod.rel", "hm_enemies.rel"};
        for(int i = 0; i < tempValidFileNames.length; i++)
        {
            validFileNames.add(tempValidFileNames[i]);
        }

        setRed0TextFieldFormats();

        //Window
        window = primaryStage;
        window.setTitle("Battle Unit Tool");

        //Menu Buttons
        HBox topMenu = new HBox();
        HBox centerMenu = new HBox();
        Button openButton = new Button("Open Directory");
        ComboBox<File> fileSelector = new ComboBox<>();
        Button exportButton = new Button("Export File");
        Button saveFilePresetButton = new Button("Save File Preset");
        Button loadFilePresetButton = new Button("Load File Preset");
        Button closeButton = new Button("Close File");
        Button aboutButton = new Button("About");
        topMenu.getChildren().addAll(openButton, aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

        //Alligning Menu Buttons to Top
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topMenu);

        fileSelector.setMaxWidth(270);

        //Scene
        Scene emptyScene = new Scene(borderPane, 800, 600);
        window.setScene(emptyScene);

        openButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select the root folder");
                givenFile = directoryChooser.showDialog(window);

                ArrayList<File> validFiles = Main.findMatchingFiles(givenFile, validFileNames);

                fileSelector.getItems().clear();
                topMenu.getChildren().clear();
                topMenu.getChildren().addAll(openButton, fileSelector, exportButton, saveFilePresetButton, loadFilePresetButton, closeButton, aboutButton);

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
                            setGraphic(null);
                        } 
                        else 
                        {
                            setText(fileNameSelector(item.getName()));
                            setGraphic(fileImageSelector(item.getName()));
                        }
                    }
                });
                fileSelector.setButtonCell(new ListCell<File>() 
                {
                    @Override protected void updateItem(File item, boolean empty) 
                    {
                        super.updateItem(item, empty);

                        if (empty || item == null) 
                        {
                            setText(null);
                            setGraphic(null);
                        } 
                        else 
                        {
                            setText(item.getName());
                            setGraphic(fileImageSelector(item.getName()));
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
                    unitSelector = new ComboBox<>();
                    for (UnitData unit : units) 
                    {
                        unitSelector.getItems().add(unit);
                    }
                    unitSelector.setMaxWidth(130);

                    unitSelector.setCellFactory(lv -> new ListCell<UnitData>()
                    {
                        @Override protected void updateItem(UnitData unit, boolean empty)
                        {
                            super.updateItem(unit, empty);

                            if(empty || unit == null)
                            {
                                setText(null);
                                setGraphic(null);
                            }
                            else
                            {
                                setText(unit.name);
                                setGraphic(unitImageSelector(unit.name));
                            }
                        }
                    });
                    unitSelector.setButtonCell(new ListCell<UnitData>() 
                    {
                        @Override protected void updateItem(UnitData unit, boolean empty) 
                        {
                            super.updateItem(unit, empty);

                            if (empty || unit == null) 
                            {
                                setText(null);
                                setGraphic(null);
                            } 
                            else 
                            {
                                setText(unit.name);
                                setGraphic(unitImageSelector(unit.name));
                            }
                        }
                    });

                    //Setting struct names for BattleUnitKind
                    kindList = new ListView<>();
                    kindList.setCellFactory(lv -> new ListCell<Object>() 
                    {
                        @Override protected void updateItem(Object item, boolean empty) 
                        {
                            super.updateItem(item, empty);
                            if (empty || item == null) 
                            {
                                setText(null);
                            } 
                            else 
                            {
                                int count = 0;
                                if (getListView().getItems() != null) 
                                {
                                    for (int i = 0; i < getIndex(); i++) 
                                    {
                                        Object previousItem = getListView().getItems().get(i);
                                        if (item instanceof BattleUnitKind && previousItem instanceof BattleUnitKind) 
                                        {
                                            count++;
                                        } 
                                        else if (item instanceof BattleUnitKindPart && previousItem instanceof BattleUnitKindPart) 
                                        {
                                            count++;
                                        }
                                    }
                                }

                                if(item instanceof BattleUnitKind)
                                {
                                    setText("Kind Struct " + (count + 1));
                                }
                                else if(item instanceof BattleUnitKindPart)
                                {
                                    setText("Part Struct " + (count + 1));
                                }
                            }
                        }
                    });

                    //Setting struct names for BattleUnitDefense and BattleUnitDefenseAttr
                    defenseList = new ListView<>();
                    defenseList.setCellFactory(lv -> new ListCell<Object>() 
                    {
                        @Override protected void updateItem(Object item, boolean empty) 
                        {
                            super.updateItem(item, empty);
                            if (empty || item == null) 
                            {
                                setText(null);
                            } 
                            else 
                            {
                                int count = 0;
                                if (getListView().getItems() != null) 
                                {
                                    for (int i = 0; i < getIndex(); i++) 
                                    {
                                        Object previousItem = getListView().getItems().get(i);
                                        if (item instanceof BattleUnitDefense && previousItem instanceof BattleUnitDefense) 
                                        {
                                            count++;
                                        } 
                                        else if (item instanceof BattleUnitDefenseAttr && previousItem instanceof BattleUnitDefenseAttr) 
                                        {
                                            count++;
                                        }
                                    }
                                }

                                if(item instanceof BattleUnitDefense)
                                {
                                    setText("Defense Struct " + (count + 1));
                                }
                                else if(item instanceof BattleUnitDefenseAttr)
                                {
                                    setText("Defense Attribute Struct " + (count + 1));
                                }
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
                                setText("Status Vulnerability Struct " + index);
                            }
                        }
                    });

                    //Setting struct names for BattleWeapon
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

                    GridPane leftMenuForm = new GridPane();
                    upperBox.setPadding(new Insets(3));
                    upperBox.getChildren().clear();
                    upperBox.getChildren().add(unitSelector);
                    upperBox.setSpacing(5);
                    upperBox.setAlignment(Pos.CENTER_LEFT);
                    leftMenuForm.add(upperBox, 0, 0);

                    upperBox2 = new HBox();
                    upperBox2.setPadding(new Insets(3));
                    upperBox2.setSpacing(3);
                    upperBox2.getChildren().clear();
                    upperBox2.getChildren().add(new Label(""));
                    upperBox2.setAlignment(Pos.CENTER);
                    leftMenuForm.add(upperBox2, 0, 1);

                    HBox baseBox = new HBox();
                    baseBox.setPadding(new Insets(3));
                    kindImageView.setFitWidth(20);
                    kindImageView.setFitHeight(20);
                    baseBox.getChildren().addAll(new Label("Base:", kindImageView));
                    leftMenuForm.add(baseBox, 0, 2);
                    leftMenuForm.add(kindList, 0, 3);

                    HBox defenseBox = new HBox();
                    defenseBox.setPadding(new Insets(3));
                    defenseImageView.setFitWidth(20);
                    defenseImageView.setFitHeight(20);
                    defenseBox.getChildren().addAll(new Label("Defense:", defenseImageView));
                    leftMenuForm.add(defenseBox, 0, 4);
                    leftMenuForm.add(defenseList, 0, 5);

                    HBox statusBox = new HBox();
                    statusBox.setPadding(new Insets(3));
                    statusImageView.setFitWidth(20);
                    statusImageView.setFitHeight(20);
                    statusBox.getChildren().addAll(new Label("Status Vulnerabilities:", statusImageView));
                    leftMenuForm.add(statusBox, 0, 6);
                    leftMenuForm.add(statuslist, 0, 7);

                    HBox weaponBox = new HBox();
                    weaponBox.setPadding(new Insets(3));
                    weaponImageView.setFitWidth(20);
                    weaponImageView.setFitHeight(20);
                    weaponBox.getChildren().addAll(new Label("Attacks:", weaponImageView));
                    leftMenuForm.add(weaponBox, 0, 8);
                    leftMenuForm.add(weaponList, 0, 9);
                    
                    //leftMenu.getChildren().addAll(unitSelector, new Label("Base:"), kindList, new Label("Defense:"), defenseList, new Label("Status Vulnerabilities:"), statuslist, new Label("Attacks:"), weaponList);
                    //leftMenu.setPadding(new Insets(5));
                    //leftMenu.setSpacing(3);
                    borderPane.setLeft(leftMenuForm);

                    GridPane form = new GridPane();

                    unitSelector.setOnAction(e2 -> 
                    {
                        centerMenu.getChildren().clear();
                        upperBox.getChildren().clear();
                        upperBox2.getChildren().clear();
                        upperBox.getChildren().add(unitSelector);
                        upperBox2.getChildren().add(new Label(""));
                        loadSelectedUnit(unitSelector.getSelectionModel().getSelectedIndex());
                    });

                    saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override public void handle(ActionEvent event)
                        {
                            saveFieldsToSelectedStruct();
                            saveChangesButton.setText("    Changes Saved     ");
                        }
                    });

                    saveStructPresetButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override public void handle(ActionEvent event)
                        {
                            Object selected = null;
                            byte[] presetFileData = null;
                            FileChooser presetFileChooser = new FileChooser();

                            if (kindList.getSelectionModel().getSelectedItem() != null) 
                            {
                                selected = kindList.getSelectionModel().getSelectedItem();
                            } 
                            else if (defenseList.getSelectionModel().getSelectedItem() != null) 
                            {
                                selected = defenseList.getSelectionModel().getSelectedItem();
                            } 
                            else if (statuslist.getSelectionModel().getSelectedItem() != null)
                            {
                                selected = statuslist.getSelectionModel().getSelectedItem();
                            }
                            else if (weaponList.getSelectionModel().getSelectedItem() != null)
                            {
                                selected = weaponList.getSelectionModel().getSelectedItem();
                            }

                            if (selected instanceof BattleUnitKind b) 
                            {
                                presetFileData = Main.buildPresetFile(givenFile, b);
                                presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().toString() + kindList.getSelectionModel().getSelectedItem().toString());
                            }
                            else if (selected instanceof BattleUnitKindPart b) 
                            {
                                presetFileData = Main.buildPresetFile(givenFile, b);
                                presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().toString() + kindList.getSelectionModel().getSelectedItem().toString());
                            }
                            else if (selected instanceof BattleUnitDefense b) 
                            {
                                presetFileData = Main.buildPresetFile(givenFile, b);
                                presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().toString() + defenseList.getSelectionModel().getSelectedItem().toString());
                            }
                            else if (selected instanceof BattleUnitDefenseAttr b) 
                            {
                                presetFileData = Main.buildPresetFile(givenFile, b);
                                presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().toString() + defenseList.getSelectionModel().getSelectedItem().toString());
                            }
                            else if (selected instanceof StatusVulnerability b) 
                            {
                                presetFileData = Main.buildPresetFile(givenFile, b);
                                presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().toString() + statuslist.getSelectionModel().getSelectedItem().toString());
                            }
                            else if (selected instanceof BattleWeapon b) 
                            {
                                presetFileData = Main.buildPresetFile(givenFile, b);
                                presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().toString() + b.attackName.replace(" ", "") + weaponList.getSelectionModel().getSelectedItem().toString());
                            }

                            FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                            presetFileChooser.getExtensionFilters().addAll(bupFilter);
                            presetFileChooser.setTitle("Save As");

                            File dest = presetFileChooser.showSaveDialog(window);
                            if (dest != null) 
                            {
                                try 
                                {
                                    FileOutputStream fos = new FileOutputStream(dest);
                                    fos.write(presetFileData);
                                    fos.close();

                                    Stage successBox = new Stage();
                                    successBox.setTitle("Export");
                                    successBox.getIcons().add(unitImage);

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

                                    test.testUnitData(units);
                                } 
                                catch (IOException ex) 
                                {
                                    System.out.println("There was an error creating the output file");
                                }
                            }
                        }
                    });

                    loadStructPresetButton.setOnAction(new EventHandler<ActionEvent>() 
                    {
                        @Override public void handle(ActionEvent event)
                        {
                            File givenPreset = null;
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Select a Preset File");
                            FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                            fileChooser.getExtensionFilters().addAll(bupFilter);
                            givenPreset = fileChooser.showOpenDialog(window);

                            byte[] givenPresetData = ByteUtils.readData(givenPreset);

                            Object selected = null;
                            if (kindList.getSelectionModel().getSelectedItem() != null) 
                            {
                                selected = kindList.getSelectionModel().getSelectedItem();
                            } 
                            else if (defenseList.getSelectionModel().getSelectedItem() != null) 
                            {
                                selected = defenseList.getSelectionModel().getSelectedItem();
                            } 
                            else if (statuslist.getSelectionModel().getSelectedItem() != null)
                            {
                                selected = statuslist.getSelectionModel().getSelectedItem();
                            }
                            else if (weaponList.getSelectionModel().getSelectedItem() != null)
                            {
                                selected = weaponList.getSelectionModel().getSelectedItem();
                            }

                            if(givenPresetData[0] == 0x00)
                            {
                                showGiveValidFileWindow();
                            }
                            else if (selected instanceof BattleUnitKind b) 
                            {
                                if(givenPresetData[0] == 0x01)
                                {
                                    ArrayList<UnitData> tempPreset = Main.getPresetData(givenPreset);
                                    savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitKindData.get(0));
                                    loadStructFields(selected);
                                    showPresetSuccessWindow();
                                }
                                else
                                {
                                    showGiveValidFileWindow();
                                }
                            }
                            else if (selected instanceof BattleUnitKindPart b) 
                            {
                                if(givenPresetData[0] == 0x02)
                                {
                                    ArrayList<UnitData> tempPreset = Main.getPresetData(givenPreset);
                                    savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitKindPartData.get(0));
                                    loadStructFields(selected);
                                    showPresetSuccessWindow();
                                }
                                else
                                {
                                    showGiveValidFileWindow();
                                }
                            }
                            else if (selected instanceof BattleUnitDefense b) 
                            {
                                if(givenPresetData[0] == 0x03)
                                {
                                    ArrayList<UnitData> tempPreset = Main.getPresetData(givenPreset);
                                    savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitDefenseData.get(0));
                                    loadStructFields(selected);
                                    showPresetSuccessWindow();
                                }
                                else
                                {
                                    showGiveValidFileWindow();
                                }
                            }
                            else if (selected instanceof BattleUnitDefenseAttr b) 
                            {
                                if(givenPresetData[0] == 0x04)
                                {
                                    ArrayList<UnitData> tempPreset = Main.getPresetData(givenPreset);
                                    savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitDefenseAttrData.get(0));
                                    loadStructFields(selected);
                                    showPresetSuccessWindow();
                                }
                                else
                                {
                                    showGiveValidFileWindow();
                                }
                            }
                            else if (selected instanceof StatusVulnerability b) 
                            {
                                if(givenPresetData[0] == 0x05)
                                {
                                    ArrayList<UnitData> tempPreset = Main.getPresetData(givenPreset);
                                    savePresetFieldsToSelectedStruct(tempPreset.get(0).StatusVulnerabilityData.get(0));
                                    loadStructFields(selected);
                                    showPresetSuccessWindow();
                                }
                                else
                                {
                                    showGiveValidFileWindow();
                                }
                            }
                            else if (selected instanceof BattleWeapon b) 
                            {
                                if(givenPresetData[0] == 0x06)
                                {
                                    ArrayList<UnitData> tempPreset = Main.getPresetData(givenPreset);
                                    savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleWeaponData.get(0));
                                    loadStructFields(selected);
                                    showPresetSuccessWindow();
                                }
                                else
                                {
                                    showGiveValidFileWindow();
                                }
                            }
                        }
                    });

                    kindList.setOnMouseClicked(e2 -> 
                    {                    
                        centerMenu.getChildren().clear();
                        form.getChildren().clear();
                        defenseList.getSelectionModel().clearSelection();
                        statuslist.getSelectionModel().clearSelection();
                        weaponList.getSelectionModel().clearSelection();

                        upperBox.getChildren().clear();
                        upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                        upperBox2.getChildren().clear();
                        upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                        if(kindList.getSelectionModel().getSelectedItem() instanceof BattleUnitKind)
                        {
                            swallowAttributeSelector.getItems().clear();
                            swallowAttributeSelector.getItems().addAll("None", "Swallow Bomb", "Spit into Other Enemy", "Spit Fireball", "Swallow Part of Enemy");

                            BattleUnitKind selected = (BattleUnitKind) kindList.getSelectionModel().getSelectedItem();

                            if (selected != null) loadStructFields(selected);

                            saveChangesButton.setText("Save Struct Changes");

                            form.setVgap(5);
                            form.setHgap(10);
                            form.setPadding(new Insets(10));

                            BUK1ImageView.setFitWidth(20); BUK1ImageView.setFitHeight(20); form.add(BUK1ImageView, 0, 0);
                            form.add(new Label("HP:"), 1, 0);
                            form.add(HPField, 2, 0);
                            BUK9ImageView.setFitWidth(20); BUK9ImageView.setFitHeight(20); form.add(BUK9ImageView, 0, 1);
                            form.add(new Label("Danger HP:"), 1, 1);
                            form.add(dangerHPField, 2, 1);
                            BUK10ImageView.setFitWidth(20); BUK10ImageView.setFitHeight(20); form.add(BUK10ImageView, 0, 2);
                            form.add(new Label("Peril HP:"), 1, 2);
                            form.add(perilHPField, 2, 2);
                            BUK2ImageView.setFitWidth(20); BUK2ImageView.setFitHeight(20); form.add(BUK2ImageView, 0, 3);
                            form.add(new Label("Level:"), 1, 3);
                            form.add(levelField, 2, 3);
                            BUK3ImageView.setFitWidth(20); BUK3ImageView.setFitHeight(20); form.add(BUK3ImageView, 0, 4);
                            form.add(new Label("Bonus XP:"), 1, 4);
                            form.add(bonusXPField, 2, 4);
                            BUK4ImageView.setFitWidth(20); BUK4ImageView.setFitHeight(20); form.add(BUK4ImageView, 0, 5);
                            form.add(new Label("Bonus Coin:"), 1, 5);
                            form.add(bonusCoinField, 2, 5);
                            BUK5ImageView.setFitWidth(20); BUK5ImageView.setFitHeight(20); form.add(BUK5ImageView, 0, 6);
                            form.add(new Label("Bonus Coin Rate:"), 1, 6);
                            form.add(bonusCoinRateField, 2, 6);
                            BUK6ImageView.setFitWidth(20); BUK6ImageView.setFitHeight(20); form.add(BUK6ImageView, 0, 7);
                            form.add(new Label("Base Coin:"), 1, 7);
                            form.add(baseCoinField, 2, 7);
                            BUK7ImageView.setFitWidth(20); BUK7ImageView.setFitHeight(20); form.add(BUK7ImageView, 0, 8);
                            form.add(new Label("Run Rate:"), 1, 8);
                            form.add(runRateField, 2, 8);
                            BUK8ImageView.setFitWidth(20); BUK8ImageView.setFitHeight(20); form.add(BUK8ImageView, 0, 9);
                            form.add(new Label("PB Cap:"), 1, 9);
                            form.add(pbCapField, 2, 9);
                            BUK11ImageView.setFitWidth(20); BUK11ImageView.setFitHeight(20); form.add(BUK11ImageView, 0, 10);
                            form.add(new Label("Swallow Chance:"), 1, 10);
                            form.add(swallowChanceField, 2, 10);
                            BUK12ImageView.setFitWidth(20); BUK12ImageView.setFitHeight(20); form.add(BUK12ImageView, 0, 11);
                            form.add(new Label("Swallow Attribute:"), 1, 11);
                            form.add(swallowAttributeSelector, 2, 11);
                            BUK13ImageView.setFitWidth(20); BUK13ImageView.setFitHeight(20); form.add(BUK13ImageView, 0, 12);
                            form.add(new Label("Hammer Knockback Chance:"), 1, 12);
                            form.add(UltraHammerKnockChanceField, 2, 12);
                            BUK14ImageView.setFitWidth(20); BUK14ImageView.setFitHeight(20); form.add(BUK14ImageView, 0, 13);
                            form.add(new Label("Item Steal Difficulty:"), 1, 13);
                            form.add(ItemStealParameterField, 2, 13);

                            centerMenu.getChildren().add(form);
                            ScrollPane centerScroll = new ScrollPane();
                            centerScroll.setContent(centerMenu);
                            borderPane.setCenter(centerScroll);
                        }
                        else if(kindList.getSelectionModel().getSelectedItem() instanceof BattleUnitKindPart)
                        {
                            BattleUnitKindPart selected = (BattleUnitKindPart) kindList.getSelectionModel().getSelectedItem();

                            if (selected != null) loadStructFields(selected);

                            saveChangesButton.setText("Save Struct Changes");

                            form.setVgap(5);
                            form.setHgap(10);
                            form.setPadding(new Insets(10));

                            form.add(new Label("Select Most Preferred Target:"), 0, 0);
                            form.add(PAF1Box, 1, 0);
                            form.add(new Label("Select Preferred Target:"), 0, 1);
                            form.add(PAF2Box, 1, 1);
                            form.add(new Label("Select Target:"), 0, 2);
                            form.add(PAF3Box, 1, 2);
                            form.add(new Label("Weak to Attack FX R:"), 0, 3);
                            form.add(PAF4Box, 1, 3);
                            form.add(new Label("Weak to Ice Power:"), 0, 4);
                            form.add(PAF5Box, 1, 4);
                            form.add(new Label("Is Winged:"), 0, 5);
                            form.add(PAF6Box, 1, 5);
                            form.add(new Label("Is Shelled:"), 0, 6);
                            form.add(PAF7Box, 1, 6);
                            form.add(new Label("Is Bomb Flippable:"), 0, 7);
                            form.add(PAF8Box, 1, 7);
                            form.add(new Label("Never Targetable:"), 0, 8);
                            form.add(PAF9Box, 1, 8);
                            form.add(new Label("Cannot be Tattled:"), 0, 9);
                            form.add(PAF10Box, 1, 9);
                            form.add(new Label("Jump-like Attacks Cannot Target:"), 0, 10);
                            form.add(PAF11Box, 1, 10);
                            form.add(new Label("Hammer-like Attacks Cannot Target:"), 0, 11);
                            form.add(PAF12Box, 1, 11);
                            form.add(new Label("Shell Toss-like Attacks Cannot Target:"), 0, 12);
                            form.add(PAF13Box, 1, 12);
                            form.add(new Label("Is Immune to Damage and Status:"), 0, 13);
                            form.add(PAF14Box, 1, 13);
                            form.add(new Label("Is Immune to OHKO:"), 0, 14);
                            form.add(PAF15Box, 1, 14);
                            form.add(new Label("Is Immune to Status:"), 0, 15);
                            form.add(PAF16Box, 1, 15);
                            form.add(new Label("Is Top Spiky:"), 0, 16);
                            form.add(PCAF1Box, 1, 16);
                            form.add(new Label("Is Preemptive Front Spiky:"), 0, 17);
                            form.add(PCAF2Box, 1, 17);
                            form.add(new Label("Is Front Spiky:"), 0, 18);
                            form.add(PCAF3Box, 1, 18);
                            form.add(new Label("Is Fiery:"), 0, 19);
                            form.add(PCAF4Box, 1, 19);
                            form.add(new Label("Has a Fiery Status:"), 0, 20);
                            form.add(PCAF5Box, 1, 20);
                            form.add(new Label("Is Icy:"), 0, 21);
                            form.add(PCAF6Box, 1, 21);
                            form.add(new Label("Has an Icy Status:"), 0, 22);
                            form.add(PCAF7Box, 1, 22);
                            form.add(new Label("Is Poisonous:"), 0, 23);
                            form.add(PCAF8Box, 1, 23);
                            form.add(new Label("Has a Poison Status:"), 0, 24);
                            form.add(PCAF9Box, 1, 24);
                            form.add(new Label("Is Electric:"), 0, 25);
                            form.add(PCAF10Box, 1, 25);
                            form.add(new Label("Has an Electric Status:"), 0, 26);
                            form.add(PCAF11Box, 1, 26);
                            form.add(new Label("Is Explosive:"), 0, 27);
                            form.add(PCAF12Box, 1, 27);
                            form.add(new Label("Is Volatile Explosive:"), 0, 28);
                            form.add(PCAF13Box, 1, 28);

                            centerMenu.getChildren().add(form);
                            ScrollPane centerScroll = new ScrollPane();
                            centerScroll.setContent(centerMenu);
                            borderPane.setCenter(centerScroll);
                        }
                    });  
                    defenseList.setOnMouseClicked(e2 -> 
                    {
                        centerMenu.getChildren().clear();
                        form.getChildren().clear();
                        kindList.getSelectionModel().clearSelection();
                        statuslist.getSelectionModel().clearSelection();
                        weaponList.getSelectionModel().clearSelection();

                        upperBox.getChildren().clear();
                        upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                        upperBox2.getChildren().clear();
                        upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                        if(defenseList.getSelectionModel().getSelectedItem() instanceof BattleUnitDefense)
                        {
                            BattleUnitDefense selected = (BattleUnitDefense) defenseList.getSelectionModel().getSelectedItem();

                            if (selected != null) loadStructFields(selected);

                            saveChangesButton.setText("Save Struct Changes");

                            form.setVgap(5);
                            form.setHgap(10);
                            form.setPadding(new Insets(10));

                            BUD1ImageView.setFitWidth(20); BUD1ImageView.setFitHeight(20); form.add(BUD1ImageView, 0, 0);
                            form.add(new Label("Normal Defense:"), 1, 0);
                            form.add(NormalField, 2, 0);
                            BUD2ImageView.setFitWidth(20); BUD2ImageView.setFitHeight(20); form.add(BUD2ImageView, 0, 1);
                            form.add(new Label("Fire Defense:"), 1, 1);
                            form.add(FireField, 2, 1);
                            BUD3ImageView.setFitWidth(20); BUD3ImageView.setFitHeight(20); form.add(BUD3ImageView, 0, 2);
                            form.add(new Label("Ice Defense:"), 1, 2);
                            form.add(IceField, 2, 2);
                            BUD4ImageView.setFitWidth(20); BUD4ImageView.setFitHeight(20); form.add(BUD4ImageView, 0, 3);
                            form.add(new Label("Explosion Defense:"), 1, 3);
                            form.add(ExplosionField, 2, 3);
                            BUD5ImageView.setFitWidth(20); BUD5ImageView.setFitHeight(20); form.add(BUD5ImageView, 0, 4);
                            form.add(new Label("Electric Defense:"), 1, 4);
                            form.add(ElectricField, 2, 4);

                            centerMenu.getChildren().add(form);
                            ScrollPane centerScroll = new ScrollPane();
                            centerScroll.setContent(centerMenu);
                            borderPane.setCenter(centerScroll);
                        }
                        else if(defenseList.getSelectionModel().getSelectedItem() instanceof BattleUnitDefenseAttr)
                        {
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

                            BattleUnitDefenseAttr selected = (BattleUnitDefenseAttr) defenseList.getSelectionModel().getSelectedItem();

                            if (selected != null) loadStructFields(selected);

                            saveChangesButton.setText("Save Struct Changes");

                            form.setVgap(5);
                            form.setHgap(10);
                            form.setPadding(new Insets(10));

                            BUDA1ImageView.setFitWidth(20); BUDA1ImageView.setFitHeight(20); form.add(BUDA1ImageView, 0, 0);
                            form.add(new Label("Normal Attribute Defense:"), 1, 0);
                            form.add(NormalSelector, 2, 0);
                            BUDA2ImageView.setFitWidth(20); BUDA2ImageView.setFitHeight(20); form.add(BUDA2ImageView, 0, 1);
                            form.add(new Label("Fire Attribute Defense:"), 1, 1);
                            form.add(FireSelector, 2, 1);
                            BUDA3ImageView.setFitWidth(20); BUDA3ImageView.setFitHeight(20); form.add(BUDA3ImageView, 0, 2);
                            form.add(new Label("Ice Attribute Defense:"), 1, 2);
                            form.add(IceSelector, 2, 2);
                            BUDA4ImageView.setFitWidth(20); BUDA4ImageView.setFitHeight(20); form.add(BUDA4ImageView, 0, 3);
                            form.add(new Label("Explosion Attribute Defense:"), 1, 3);
                            form.add(ExplosionSelector, 2, 3);
                            BUDA5ImageView.setFitWidth(20); BUDA5ImageView.setFitHeight(20); form.add(BUDA5ImageView, 0, 4);
                            form.add(new Label("Electric Attribute Defense:"), 1, 4);
                            form.add(ElectricSelector, 2, 4);

                            centerMenu.getChildren().add(form);
                            ScrollPane centerScroll = new ScrollPane();
                            centerScroll.setContent(centerMenu);
                            borderPane.setCenter(centerScroll);
                        }
                    });
                    statuslist.setOnMouseClicked(e2 -> 
                    {
                        centerMenu.getChildren().clear();
                        form.getChildren().clear();
                        kindList.getSelectionModel().clearSelection();
                        defenseList.getSelectionModel().clearSelection();
                        weaponList.getSelectionModel().clearSelection();

                        upperBox.getChildren().clear();
                        upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                        upperBox2.getChildren().clear();
                        upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                        StatusVulnerability selected = statuslist.getSelectionModel().getSelectedItem();

                        if (selected != null) loadStructFields(selected);

                        saveChangesButton.setText("Save Struct Changes");

                        form.setVgap(5);
                        form.setHgap(10);
                        form.setPadding(new Insets(10));

                        SV1ImageView.setFitWidth(20); SV1ImageView.setFitHeight(20); form.add(SV1ImageView, 0, 0);
                        form.add(new Label("Sleep Susceptibility:"), 1, 0);
                        form.add(SleepField, 2, 0);
                        SV2ImageView.setFitWidth(20); SV2ImageView.setFitHeight(20); form.add(SV2ImageView, 0, 1);
                        form.add(new Label("Stop Susceptibility:"), 1, 1);
                        form.add(StopField, 2, 1);
                        SV3ImageView.setFitWidth(20); SV3ImageView.setFitHeight(20); form.add(SV3ImageView, 0, 2);
                        form.add(new Label("Dizzy Susceptibility:"), 1, 2);
                        form.add(DizzyField, 2, 2);
                        SV4ImageView.setFitWidth(20); SV4ImageView.setFitHeight(20); form.add(SV4ImageView, 0, 3);
                        form.add(new Label("Poison Susceptibility:"), 1, 3);
                        form.add(PoisonField, 2, 3);
                        SV5ImageView.setFitWidth(20); SV5ImageView.setFitHeight(20); form.add(SV5ImageView, 0, 4);
                        form.add(new Label("Confuse Susceptibility:"), 1, 4);
                        form.add(ConfuseField, 2, 4);
                        SV6ImageView.setFitWidth(20); SV6ImageView.setFitHeight(20); form.add(SV6ImageView, 0, 5);
                        form.add(new Label("Electric Susceptibility:"), 1, 5);
                        form.add(ElectricField, 2, 5);
                        SV7ImageView.setFitWidth(20); SV7ImageView.setFitHeight(20); form.add(SV7ImageView, 0, 6);
                        form.add(new Label("Burn Susceptibility:"), 1, 6);
                        form.add(BurnField, 2, 6);
                        SV8ImageView.setFitWidth(20); SV8ImageView.setFitHeight(20); form.add(SV8ImageView, 0, 7);
                        form.add(new Label("Freeze Susceptibility:"), 1, 7);
                        form.add(FreezeField, 2, 7);
                        SV9ImageView.setFitWidth(20); SV9ImageView.setFitHeight(20); form.add(SV9ImageView, 0, 8);
                        form.add(new Label("Huge Susceptibility:"), 1, 8);
                        form.add(HugeField, 2, 8);
                        SV10ImageView.setFitWidth(20); SV10ImageView.setFitHeight(20); form.add(SV10ImageView, 0, 9);
                        form.add(new Label("Tiny Susceptibility:"), 1, 9);
                        form.add(TinyField, 2, 9);
                        SV11ImageView.setFitWidth(20); SV11ImageView.setFitHeight(20); form.add(SV11ImageView, 0, 10);
                        form.add(new Label("Attack Up Susceptibility:"), 1, 10);
                        form.add(AttackUpField, 2, 10);
                        SV12ImageView.setFitWidth(20); SV12ImageView.setFitHeight(20); form.add(SV12ImageView, 0, 11);
                        form.add(new Label("Attack Down Susceptibility:"), 1, 11);
                        form.add(AttackDownField, 2, 11);
                        SV13ImageView.setFitWidth(20); SV13ImageView.setFitHeight(20); form.add(SV13ImageView, 0, 12);
                        form.add(new Label("Defense Up Susceptibility:"), 1, 12);
                        form.add(DefenseUpField, 2, 12);
                        SV14ImageView.setFitWidth(20); SV14ImageView.setFitHeight(20); form.add(SV14ImageView, 0, 13);
                        form.add(new Label("Defense Down Susceptibility:"), 1, 13);
                        form.add(DefenseDownField, 2, 13);
                        SV15ImageView.setFitWidth(20); SV15ImageView.setFitHeight(20); form.add(SV15ImageView, 0, 14);
                        form.add(new Label("Allergic Susceptibility:"), 1, 14);
                        form.add(AllergicField, 2, 14);
                        SV16ImageView.setFitWidth(20); SV16ImageView.setFitHeight(20); form.add(SV16ImageView, 0, 15);
                        form.add(new Label("Fright Susceptibility:"), 1, 15);
                        form.add(FrightField, 2, 15);
                        SV17ImageView.setFitWidth(20); SV17ImageView.setFitHeight(20); form.add(SV17ImageView, 0, 16);
                        form.add(new Label("Gale Force Susceptibility:"), 1, 16);
                        form.add(GaleForceField, 2, 16);
                        SV18ImageView.setFitWidth(20); SV18ImageView.setFitHeight(20); form.add(SV18ImageView, 0, 17);
                        form.add(new Label("Fast Susceptibility:"), 1, 17);
                        form.add(FastField, 2, 17);
                        SV19ImageView.setFitWidth(20); SV19ImageView.setFitHeight(20); form.add(SV19ImageView, 0, 18);
                        form.add(new Label("Slow Susceptibility:"), 1, 18);
                        form.add(SlowField, 2, 18);
                        SV20ImageView.setFitWidth(20); SV20ImageView.setFitHeight(20); form.add(SV20ImageView, 0, 19);
                        form.add(new Label("Dodgy Susceptibility:"), 1, 19);
                        form.add(DodgyField, 2, 19);
                        SV21ImageView.setFitWidth(20); SV21ImageView.setFitHeight(20); form.add(SV21ImageView, 0, 20);
                        form.add(new Label("Invisible Susceptibility:"), 1, 20);
                        form.add(InvisibleField, 2, 20);
                        SV22ImageView.setFitWidth(20); SV22ImageView.setFitHeight(20); form.add(SV22ImageView, 0, 21);
                        form.add(new Label("OHKO Susceptibility:"), 1, 21);
                        form.add(OHKOField, 2, 21);

                        centerMenu.getChildren().add(form);
                        ScrollPane centerScroll = new ScrollPane();
                        centerScroll.setContent(centerMenu);
                        borderPane.setCenter(centerScroll);
                    });
                    weaponList.setOnMouseClicked(e2 -> 
                    {
                        centerMenu.getChildren().clear();
                        form.getChildren().clear();
                        kindList.getSelectionModel().clearSelection();
                        defenseList.getSelectionModel().clearSelection();
                        statuslist.getSelectionModel().clearSelection();

                        upperBox.getChildren().clear();
                        upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                        upperBox2.getChildren().clear();
                        upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                        SuperguardStateSelector.getItems().clear();
                        ElementSelector.getItems().clear();
                        DamagePatternSelector.getItems().clear();
                        ACLevelSelector.getItems().clear();
                        SuperguardStateSelector.getItems().addAll("Unsuperguardable", "Superguardable with Recoil", "Superguardable without Recoil");
                        ElementSelector.getItems().addAll("Normal", "Fire", "Ice", "Explosive", "Electric");
                        DamagePatternSelector.getItems().addAll("None", "???", "Gulp Knocked onto Ground", "Gulp Knocked into next Target", "???", "Squashed Type 1", "Squashed Type 2", "Squashed Type 3", "Inked", "Super Hammer Knocked Back", "Ultra Hammer Knocked Back", "???", "Confusion Effect", "Spin Once 1", "Spin Once 2", "Quickly Spin 1", "Quickly Spin 2", "Blown Away", "???");
                        ACLevelSelector.getItems().addAll("Extremely Easy", "Very Easy", "Easy", "Normal", "Difficult", "Very Difficult", "Extremely Difficult");

                        BattleWeapon selected = weaponList.getSelectionModel().getSelectedItem();

                        if (selected != null) loadStructFields(selected);

                        saveChangesButton.setText("Save Struct Changes");

                        form.setVgap(5);
                        form.setHgap(10);
                        form.setPadding(new Insets(10));

                        BW1ImageView.setFitWidth(20); BW1ImageView.setFitHeight(20); form.add(BW1ImageView, 0, 0);
                        form.add(new Label("Accuracy:"), 1, 0);
                        form.add(AccuracyField, 2, 0);
                        BW2ImageView.setFitWidth(20); BW2ImageView.setFitHeight(20); form.add(BW2ImageView, 0, 1);
                        form.add(new Label("FP Cost:"), 1, 1);
                        form.add(FPCostField, 2, 1);
                        BW3ImageView.setFitWidth(20); BW3ImageView.setFitHeight(20); form.add(BW3ImageView, 0, 2);
                        form.add(new Label("SP Cost:"), 1, 2);
                        form.add(SPCostField, 2, 2);
                        BW4ImageView.setFitWidth(20); BW4ImageView.setFitHeight(20); form.add(BW4ImageView, 0, 3);
                        form.add(new Label("Superguard State:"), 1, 3);
                        form.add(SuperguardStateSelector, 2, 3);
                        BW5ImageView.setFitWidth(20); BW5ImageView.setFitHeight(20); form.add(BW5ImageView, 0, 4);
                        form.add(new Label("Stylish Multiplier:"), 1, 4);
                        form.add(StylishMultiplierField, 2, 4);
                        BW6ImageView.setFitWidth(20); BW6ImageView.setFitHeight(20); form.add(BW6ImageView, 0, 5);
                        form.add(new Label("Increase Bingo Slot Chance:"), 1, 5);
                        form.add(BingoSlotIncChanceField, 2, 5);
                        BW7ImageView.setFitWidth(20); BW7ImageView.setFitHeight(20); form.add(BW7ImageView, 0, 6);
                        form.add(new Label("Base Damage Parameter 1:"), 1, 6);
                        form.add(BaseDamage1Field, 2, 6);
                        BW8ImageView.setFitWidth(20); BW8ImageView.setFitHeight(20); form.add(BW8ImageView, 0, 7);
                        form.add(new Label("Base Damage Parameter 2:"), 1, 7);
                        form.add(BaseDamage2Field, 2, 7);
                        BW9ImageView.setFitWidth(20); BW9ImageView.setFitHeight(20); form.add(BW9ImageView, 0, 8);
                        form.add(new Label("Base Damage Parameter 3:"), 1, 8);
                        form.add(BaseDamage3Field, 2, 8);
                        BW10ImageView.setFitWidth(20); BW10ImageView.setFitHeight(20); form.add(BW10ImageView, 0, 9);
                        form.add(new Label("Base Damage Parameter 4:"), 1, 9);
                        form.add(BaseDamage4Field, 2, 9);
                        BW11ImageView.setFitWidth(20); BW11ImageView.setFitHeight(20); form.add(BW11ImageView, 0, 10);
                        form.add(new Label("Base Damage Parameter 5:"), 1, 10);
                        form.add(BaseDamage5Field, 2, 10);
                        BW12ImageView.setFitWidth(20); BW12ImageView.setFitHeight(20); form.add(BW12ImageView, 0, 11);
                        form.add(new Label("Base Damage Parameter 6:"), 1, 11);
                        form.add(BaseDamage6Field, 2, 11);
                        BW13ImageView.setFitWidth(20); BW13ImageView.setFitHeight(20); form.add(BW13ImageView, 0, 12);
                        form.add(new Label("Base Damage Parameter 7:"), 1, 12);
                        form.add(BaseDamage7Field, 2, 12);
                        BW14ImageView.setFitWidth(20); BW14ImageView.setFitHeight(20); form.add(BW14ImageView, 0, 13);
                        form.add(new Label("Base Damage Parameter 8:"), 1, 13);
                        form.add(BaseDamage8Field, 2, 13);
                        BW15ImageView.setFitWidth(20); BW15ImageView.setFitHeight(20); form.add(BW15ImageView, 0, 14);
                        form.add(new Label("Base FP Damage Parameter 1:"), 1, 14);
                        form.add(BaseFPDamage1Field, 2, 14);
                        BW16ImageView.setFitWidth(20); BW16ImageView.setFitHeight(20); form.add(BW16ImageView, 0, 15);
                        form.add(new Label("Base FP Damage Parameter 2:"), 1, 15);
                        form.add(BaseFPDamage2Field, 2, 15);
                        BW17ImageView.setFitWidth(20); BW17ImageView.setFitHeight(20); form.add(BW17ImageView, 0, 16);
                        form.add(new Label("Base FP Damage Parameter 3:"), 1, 16);
                        form.add(BaseFPDamage3Field, 2, 16);
                        BW18ImageView.setFitWidth(20); BW18ImageView.setFitHeight(20); form.add(BW18ImageView, 0, 17);
                        form.add(new Label("Base FP Damage Parameter 4:"), 1, 17);
                        form.add(BaseFPDamage4Field, 2, 17);
                        BW19ImageView.setFitWidth(20); BW19ImageView.setFitHeight(20); form.add(BW19ImageView, 0, 18);
                        form.add(new Label("Base FP Damage Parameter 5:"), 1, 18);
                        form.add(BaseFPDamage5Field, 2, 18);
                        BW20ImageView.setFitWidth(20); BW20ImageView.setFitHeight(20); form.add(BW20ImageView, 0, 19);
                        form.add(new Label("Base FP Damage Parameter 6:"), 1, 19);
                        form.add(BaseFPDamage6Field, 2, 19);
                        BW21ImageView.setFitWidth(20); BW21ImageView.setFitHeight(20); form.add(BW21ImageView, 0, 20);
                        form.add(new Label("Base FP Damage Parameter 7:"), 1, 20);
                        form.add(BaseFPDamage7Field, 2, 20);
                        BW22ImageView.setFitWidth(20); BW22ImageView.setFitHeight(20); form.add(BW22ImageView, 0, 21);
                        form.add(new Label("Base FP Damage Parameter 8:"), 1, 21);
                        form.add(BaseFPDamage8Field, 2, 21);
                        BW23ImageView.setFitWidth(20); BW23ImageView.setFitHeight(20); form.add(BW23ImageView, 0, 22);
                        form.add(new Label("Element:"), 1, 22);
                        form.add(ElementSelector, 2, 22);
                        BW24ImageView.setFitWidth(20); BW24ImageView.setFitHeight(20); form.add(BW24ImageView, 0, 23);
                        form.add(new Label("Damage Pattern:"), 1, 23);
                        form.add(DamagePatternSelector, 2, 23);
                        BW79ImageView.setFitWidth(20); BW79ImageView.setFitHeight(20); form.add(BW79ImageView, 0, 24);
                        form.add(new Label("Action Command Difficulty:"), 1, 24);
                        form.add(ACLevelSelector, 2, 24);
                        BW25ImageView.setFitWidth(20); BW25ImageView.setFitHeight(20); form.add(BW25ImageView, 0, 25);
                        form.add(new Label("Sleep Chance:"), 1, 25);
                        form.add(SleepChanceField, 2, 25);
                        BW26ImageView.setFitWidth(20); BW26ImageView.setFitHeight(20); form.add(BW26ImageView, 0, 26);
                        form.add(new Label("Sleep Time:"), 1, 26);
                        form.add(SleepTimeField, 2, 26);
                        BW27ImageView.setFitWidth(20); BW27ImageView.setFitHeight(20); form.add(BW27ImageView, 0, 27);
                        form.add(new Label("Stop Chance:"), 1, 27);
                        form.add(StopChanceField, 2, 27);
                        BW28ImageView.setFitWidth(20); BW28ImageView.setFitHeight(20); form.add(BW28ImageView, 0, 28);
                        form.add(new Label("Stop Time:"), 1, 28);
                        form.add(StopTimeField, 2, 28);
                        BW29ImageView.setFitWidth(20); BW29ImageView.setFitHeight(20); form.add(BW29ImageView, 0, 29);
                        form.add(new Label("Dizzy Chance:"), 1, 29);
                        form.add(DizzyChanceField, 2, 29);
                        BW30ImageView.setFitWidth(20); BW30ImageView.setFitHeight(20); form.add(BW30ImageView, 0, 30);
                        form.add(new Label("Dizzy Time:"), 1, 30);
                        form.add(DizzyTimeField, 2, 30);
                        BW31ImageView.setFitWidth(20); BW31ImageView.setFitHeight(20); form.add(BW31ImageView, 0, 31);
                        form.add(new Label("Poison Chance:"), 1, 31);
                        form.add(PoisonChanceField, 2, 31);
                        BW32ImageView.setFitWidth(20); BW32ImageView.setFitHeight(20); form.add(BW32ImageView, 0, 32);
                        form.add(new Label("Poison Time:"), 1, 32);
                        form.add(PoisonTimeField, 2, 32);
                        BW33ImageView.setFitWidth(20); BW33ImageView.setFitHeight(20); form.add(BW33ImageView, 0, 33);
                        form.add(new Label("Poison Strength:"), 1, 33);
                        form.add(PoisonStrengthField, 2, 33);
                        BW34ImageView.setFitWidth(20); BW34ImageView.setFitHeight(20); form.add(BW34ImageView, 0, 34);
                        form.add(new Label("Confuse Chance:"), 1, 34);
                        form.add(ConfuseChanceField, 2, 34);
                        BW35ImageView.setFitWidth(20); BW35ImageView.setFitHeight(20); form.add(BW35ImageView, 0, 35);
                        form.add(new Label("Confuse Time:"), 1, 35);
                        form.add(ConfuseTimeField, 2, 35);
                        BW36ImageView.setFitWidth(20); BW36ImageView.setFitHeight(20); form.add(BW36ImageView, 0, 36);
                        form.add(new Label("Electric Chance:"), 1, 36);
                        form.add(ElectricChanceField, 2, 36);
                        BW37ImageView.setFitWidth(20); BW37ImageView.setFitHeight(20); form.add(BW37ImageView, 0, 37);
                        form.add(new Label("Electric Time:"), 1, 37);
                        form.add(ElectricTimeField, 2, 37);
                        BW38ImageView.setFitWidth(20); BW38ImageView.setFitHeight(20); form.add(BW38ImageView, 0, 38);
                        form.add(new Label("Dodgy Chance:"), 1, 38);
                        form.add(DodgyChanceField, 2, 38);
                        BW39ImageView.setFitWidth(20); BW39ImageView.setFitHeight(20); form.add(BW39ImageView, 0, 39);
                        form.add(new Label("Dodgy Time:"), 1, 39);
                        form.add(DodgyTimeField, 2, 39);
                        BW40ImageView.setFitWidth(20); BW40ImageView.setFitHeight(20); form.add(BW40ImageView, 0, 40);
                        form.add(new Label("Burn Chance:"), 1, 40);
                        form.add(BurnChanceField, 2, 40);
                        BW41ImageView.setFitWidth(20); BW41ImageView.setFitHeight(20); form.add(BW41ImageView, 0, 41);
                        form.add(new Label("Burn Time:"), 1, 41);
                        form.add(BurnTimeField, 2, 41);
                        BW42ImageView.setFitWidth(20); BW42ImageView.setFitHeight(20); form.add(BW42ImageView, 0, 42);
                        form.add(new Label("Freeze Chance:"), 1, 42);
                        form.add(FreezeChanceField, 2, 42);
                        BW43ImageView.setFitWidth(20); BW43ImageView.setFitHeight(20); form.add(BW43ImageView, 0, 43);
                        form.add(new Label("Freeze Time:"), 1, 43);
                        form.add(FreezeTimeField, 2, 43);
                        BW44ImageView.setFitWidth(20); BW44ImageView.setFitHeight(20); form.add(BW44ImageView, 0, 44);
                        form.add(new Label("Size Change Chance:"), 1, 44);
                        form.add(SizeChangeChanceField, 2, 44);
                        BW45ImageView.setFitWidth(20); BW45ImageView.setFitHeight(20); form.add(BW45ImageView, 0, 45);
                        form.add(new Label("Size Change Time:"), 1, 45);
                        form.add(SizeChangeTimeField, 2, 45);
                        BW46ImageView.setFitWidth(20); BW46ImageView.setFitHeight(20); form.add(BW46ImageView, 0, 46);
                        form.add(new Label("Size Change Strength:"), 1, 46);
                        form.add(SizeChangeStrengthField, 2, 46);
                        BW47ImageView.setFitWidth(20); BW47ImageView.setFitHeight(20); form.add(BW47ImageView, 0, 47);
                        form.add(new Label("Atk Change Chance:"), 1, 47);
                        form.add(AtkChangeChanceField, 2, 47);
                        BW48ImageView.setFitWidth(20); BW48ImageView.setFitHeight(20); form.add(BW48ImageView, 0, 48);
                        form.add(new Label("Atk Change Time:"), 1, 48);
                        form.add(AtkChangeTimeField, 2, 48);
                        BW49ImageView.setFitWidth(20); BW49ImageView.setFitHeight(20); form.add(BW49ImageView, 0, 49);
                        form.add(new Label("Atk Change Strength:"), 1, 49);
                        form.add(AtkChangeStrengthField, 2, 49);
                        BW50ImageView.setFitWidth(20); BW50ImageView.setFitHeight(20); form.add(BW50ImageView, 0, 50);
                        form.add(new Label("Def Change Chance:"), 1, 50);
                        form.add(DefChangeChanceField, 2, 50);
                        BW51ImageView.setFitWidth(20); BW51ImageView.setFitHeight(20); form.add(BW51ImageView, 0, 51);
                        form.add(new Label("Def Change Time:"), 1, 51);
                        form.add(DefChangeTimeField, 2, 51);
                        BW52ImageView.setFitWidth(20); BW52ImageView.setFitHeight(20); form.add(BW52ImageView, 0, 52);
                        form.add(new Label("Def Change Strength:"), 1, 52);
                        form.add(DefChangeStrengthField, 2, 52);
                        BW53ImageView.setFitWidth(20); BW53ImageView.setFitHeight(20); form.add(BW53ImageView, 0, 53);
                        form.add(new Label("Allergic Chance:"), 1, 53);
                        form.add(AllergicChanceField, 2, 53);
                        BW54ImageView.setFitWidth(20); BW54ImageView.setFitHeight(20); form.add(BW54ImageView, 0, 54);
                        form.add(new Label("Allergic Time:"), 1, 54);
                        form.add(AllergicTimeField, 2, 54);
                        BW55ImageView.setFitWidth(20); BW55ImageView.setFitHeight(20); form.add(BW55ImageView, 0, 55);
                        form.add(new Label("OHKO Chance:"), 1, 55);
                        form.add(OHKOChanceField, 2, 55);
                        BW56ImageView.setFitWidth(20); BW56ImageView.setFitHeight(20); form.add(BW56ImageView, 0, 56);
                        form.add(new Label("Charge Strength:"), 1, 56);
                        form.add(ChargeStrengthField, 2, 56);
                        BW57ImageView.setFitWidth(20); BW57ImageView.setFitHeight(20); form.add(BW57ImageView, 0, 57);
                        form.add(new Label("Fast Chance:"), 1, 57);
                        form.add(FastChanceField, 2, 57);
                        BW58ImageView.setFitWidth(20); BW58ImageView.setFitHeight(20); form.add(BW58ImageView, 0, 58);
                        form.add(new Label("Fast Time:"), 1, 58);
                        form.add(FastTimeField, 2, 58);
                        BW59ImageView.setFitWidth(20); BW59ImageView.setFitHeight(20); form.add(BW59ImageView, 0, 59);
                        form.add(new Label("Slow Chance:"), 1, 59);
                        form.add(SlowChanceField, 2, 59);
                        BW60ImageView.setFitWidth(20); BW60ImageView.setFitHeight(20); form.add(BW60ImageView, 0, 60);
                        form.add(new Label("Slow Time:"), 1, 60);
                        form.add(SlowTimeField, 2, 60);
                        BW61ImageView.setFitWidth(20); BW61ImageView.setFitHeight(20); form.add(BW61ImageView, 0, 61);
                        form.add(new Label("Fright Chance:"), 1, 61);
                        form.add(FrightChanceField, 2, 61);
                        BW62ImageView.setFitWidth(20); BW62ImageView.setFitHeight(20); form.add(BW62ImageView, 0, 62);
                        form.add(new Label("Gale Force Chance:"), 1, 62);
                        form.add(GaleForceChanceField, 2, 62);
                        BW63ImageView.setFitWidth(20); BW63ImageView.setFitHeight(20); form.add(BW63ImageView, 0, 63);
                        form.add(new Label("Payback Time:"), 1, 63);
                        form.add(PaybackTimeField, 2, 63);
                        BW64ImageView.setFitWidth(20); BW64ImageView.setFitHeight(20); form.add(BW64ImageView, 0, 64);
                        form.add(new Label("Hold Fast Time:"), 1, 64);
                        form.add(HoldFastTimeField, 2, 64);
                        BW65ImageView.setFitWidth(20); BW65ImageView.setFitHeight(20); form.add(BW65ImageView, 0, 65);
                        form.add(new Label("Invisible Chance:"), 1, 65);
                        form.add(InvisibleChanceField, 2, 65);
                        BW66ImageView.setFitWidth(20); BW66ImageView.setFitHeight(20); form.add(BW66ImageView, 0, 66);
                        form.add(new Label("Invisible Time:"), 1, 66);
                        form.add(InvisibleTimeField, 2, 66);
                        BW67ImageView.setFitWidth(20); BW67ImageView.setFitHeight(10); form.add(BW67ImageView, 0, 67);
                        form.add(new Label("HP Regen Time:"), 1, 67);
                        form.add(HPRegenTimeField, 2, 67);
                        BW68ImageView.setFitWidth(20); BW68ImageView.setFitHeight(10); form.add(BW68ImageView, 0, 68);
                        form.add(new Label("HP Regen Strength:"), 1, 68);
                        form.add(HPRegenStrengthField, 2, 68);
                        BW69ImageView.setFitWidth(20); BW69ImageView.setFitHeight(10); form.add(BW69ImageView, 0, 69);
                        form.add(new Label("FP Regen Time:"), 1, 69);
                        form.add(FPRegenTimeField, 2, 69);
                        BW70ImageView.setFitWidth(20); BW70ImageView.setFitHeight(10); form.add(BW70ImageView, 0, 70);
                        form.add(new Label("FP Regen Strength:"), 1, 70);
                        form.add(FPRegenStrengthField, 2, 70);
                        BW71ImageView.setFitWidth(20); BW71ImageView.setFitHeight(20); form.add(BW71ImageView, 0, 71);
                        form.add(new Label("Stage Background Fall Chance 1:"), 1, 71);
                        form.add(STGFall1Field, 2, 71);
                        BW72ImageView.setFitWidth(20); BW72ImageView.setFitHeight(20); form.add(BW72ImageView, 0, 72);
                        form.add(new Label("Stage Background Fall Chance 2:"), 1, 72);
                        form.add(STGFall2Field, 2, 72);
                        BW73ImageView.setFitWidth(20); BW73ImageView.setFitHeight(20); form.add(BW73ImageView, 0, 73);
                        form.add(new Label("Stage Background Fall Chance 3:"), 1, 73);
                        form.add(STGFall3Field, 2, 73);
                        BW74ImageView.setFitWidth(20); BW74ImageView.setFitHeight(20); form.add(BW74ImageView, 0, 74);
                        form.add(new Label("Stage Background Fall Chance 4:"), 1, 74);
                        form.add(STGFall4Field, 2, 74);
                        BW75ImageView.setFitWidth(20); BW75ImageView.setFitHeight(20); form.add(BW75ImageView, 0, 75);
                        form.add(new Label("Stage Nozzle Turn Chance:"), 1, 75);
                        form.add(STGNozzleTurnField, 2, 75);
                        BW76ImageView.setFitWidth(20); BW76ImageView.setFitHeight(20); form.add(BW76ImageView, 0, 76);
                        form.add(new Label("Stage Nozzle Fire Chance:"), 1, 76);
                        form.add(STGNozzleFireField, 2, 76);
                        BW77ImageView.setFitWidth(20); BW77ImageView.setFitHeight(20); form.add(BW77ImageView, 0, 77);
                        form.add(new Label("Stage Ceiling Fall Chance:"), 1, 77);
                        form.add(STGCeilingFallField, 2, 77);
                        BW78ImageView.setFitWidth(20); BW78ImageView.setFitHeight(20); form.add(BW78ImageView, 0, 78);
                        form.add(new Label("Stage Object Fall Chance:"), 1, 78);
                        form.add(STGObjectFallField, 2, 78);
                        form.add(new Label("Cannot Target Mario or Shell Shield:"), 1, 79);
                        form.add(TCF1Box, 2, 79);
                        form.add(new Label("Cannot Target Partner:"), 1, 80);
                        form.add(TCF2Box, 2, 80);
                        form.add(new Label("Cannot Target Enemy:"), 1, 81);
                        form.add(TCF3Box, 2, 81);
                        form.add(new Label("Cannot Target Opposite Alliance:"), 1, 82);
                        form.add(TCF4Box, 2, 82);
                        form.add(new Label("Cannot Target Own Alliance:"), 1, 83);
                        form.add(TCF5Box, 2, 83);
                        form.add(new Label("Cannot Target Self:"), 1, 84);
                        form.add(TCF6Box, 2, 84);
                        form.add(new Label("Cannot Target Same Species:"), 1, 85);
                        form.add(TCF7Box, 2, 85);
                        form.add(new Label("Only Target Self:"), 1, 86);
                        form.add(TCF8Box, 2, 86);
                        form.add(new Label("Only Target Mario:"), 1, 87);
                        form.add(TCF9Box, 2, 87);
                        form.add(new Label("Single Target:"), 1, 88);
                        form.add(TCF10Box, 2, 88);
                        form.add(new Label("Multiple Target:"), 1, 89);
                        form.add(TCF11Box, 2, 89);
                        form.add(new Label("Is Tattle-like:"), 1, 90);
                        form.add(TPF1Box, 2, 90);
                        form.add(new Label("Cannot Target Ceiling:"), 1, 91);
                        form.add(TPF2Box, 2, 91);
                        form.add(new Label("Cannot Target Floating:"), 1, 92);
                        form.add(TPF3Box, 2, 92);
                        form.add(new Label("Cannot Target Grounded:"), 1, 93);
                        form.add(TPF4Box, 2, 93);
                        form.add(new Label("Is Jump-like:"), 1, 94);
                        form.add(TPF5Box, 2, 94);
                        form.add(new Label("Is Hammer-like:"), 1, 95);
                        form.add(TPF6Box, 2, 95);
                        form.add(new Label("Is Shell Toss-like:"), 1, 96);
                        form.add(TPF7Box, 2, 96);
                        form.add(new Label("Has Recoil Damage:"), 1, 97);
                        form.add(TPF8Box, 2, 97);
                        form.add(new Label("Can Only Target Frontmost:"), 1, 98);
                        form.add(TPF9Box, 2, 98);
                        form.add(new Label("Target Same Alliance Direction:"), 1, 99);
                        form.add(TPF10Box, 2, 99);
                        form.add(new Label("Target Opposite Alliance Direction:"), 1, 100);
                        form.add(TPF11Box, 2, 100);
                        form.add(new Label("Badge Can Affect Power:"), 1, 101);
                        form.add(SPF1Box, 2, 101);
                        form.add(new Label("Status Can Affect Power:"), 1, 102);
                        form.add(SPF2Box, 2, 102);
                        form.add(new Label("Is Chargeable:"), 1, 103);
                        form.add(SPF3Box, 2, 103);
                        form.add(new Label("Cannot Miss:"), 1, 104);
                        form.add(SPF4Box, 2, 104);
                        form.add(new Label("Has Diminishing Damage by Hit:"), 1, 105);
                        form.add(SPF5Box, 2, 105);
                        form.add(new Label("Has Diminishing Damage by Target:"), 1, 106);
                        form.add(SPF6Box, 2, 106);
                        form.add(new Label("Pierces Defense:"), 1, 107);
                        form.add(SPF7Box, 2, 107);
                        form.add(new Label("Ingore Target Status Vulnerability:"), 1, 108);
                        form.add(SPF8Box, 2, 108);
                        form.add(new Label("Change Element to Fire if Target Burned:"), 1, 109);
                        form.add(SPF9Box, 2, 109);
                        form.add(new Label("Flips Shell Enemies:"), 1, 110);
                        form.add(SPF10Box, 2, 110);
                        form.add(new Label("Flips Bomb-Flippable Enemies:"), 1, 111);
                        form.add(SPF11Box, 2, 111);
                        form.add(new Label("Grounds Winged Enemies:"), 1, 112);
                        form.add(SPF12Box, 2, 112);
                        form.add(new Label("Can be Used when Confused:"), 1, 113);
                        form.add(SPF13Box, 2, 113);
                        form.add(new Label("Is Unguardable:"), 1, 114);
                        form.add(SPF14Box, 2, 114);
                        form.add(new Label("Ignore Electric Resistance:"), 1, 115);
                        form.add(CRF1Box, 2, 115);
                        form.add(new Label("Ignore Top Spiky Resistance:"), 1, 116);
                        form.add(CRF2Box, 2, 116);
                        form.add(new Label("Ignore Pre-emptive Front Spiky Resistance:"), 1, 117);
                        form.add(CRF3Box, 2, 117);
                        form.add(new Label("Ignore Front Spiky Resistance:"), 1, 118);
                        form.add(CRF4Box, 2, 118);
                        form.add(new Label("Ignore Fire Resistance:"), 1, 119);
                        form.add(CRF5Box, 2, 119);
                        form.add(new Label("Ignore Ice Resistance:"), 1, 120);
                        form.add(CRF6Box, 2, 120);
                        form.add(new Label("Ignore Poison Resistance:"), 1, 121);
                        form.add(CRF7Box, 2, 121);
                        form.add(new Label("Ignore Explosive Resistance:"), 1, 122);
                        form.add(CRF8Box, 2, 122);
                        form.add(new Label("Ignore Volatile Explosive Resistance:"), 1, 123);
                        form.add(CRF9Box, 2, 123);
                        form.add(new Label("Ignore Payback Resistance:"), 1, 124);
                        form.add(CRF10Box, 2, 124);
                        form.add(new Label("Ignore Hold Fast Resistance:"), 1, 125);
                        form.add(CRF11Box, 2, 125);
                        form.add(new Label("Prefer Targeting Mario:"), 1, 126);
                        form.add(TWF1Box, 2, 126);
                        form.add(new Label("Prefer Targeting Partner:"), 1, 127);
                        form.add(TWF2Box, 2, 127);
                        form.add(new Label("Prefer Targeting Front:"), 1, 128);
                        form.add(TWF3Box, 2, 128);
                        form.add(new Label("Prefer Targeting Back:"), 1, 129);
                        form.add(TWF4Box, 2, 129);
                        form.add(new Label("Prefer Targeting Same Alliance:"), 1, 130);
                        form.add(TWF5Box, 2, 130);
                        form.add(new Label("Prefer Targeting Opposite Alliance:"), 1, 131);
                        form.add(TWF6Box, 2, 131);
                        form.add(new Label("Prefer Targeting Less Healthy:"), 1, 132);
                        form.add(TWF7Box, 2, 132);
                        form.add(new Label("Greatly Prefer Targeting Less Healthy:"), 1, 133);
                        form.add(TWF8Box, 2, 133);
                        form.add(new Label("Prefer Targeting Lower HP Target:"), 1, 134);
                        form.add(TWF9Box, 2, 134);
                        form.add(new Label("Prefer Targeting Higher HP Target:"), 1, 135);
                        form.add(TWF10Box, 2, 135);
                        form.add(new Label("Prefer Targeting Target in Peril:"), 1, 136);
                        form.add(TWF11Box, 2, 136);
                        form.add(new Label("Choose Targeting Randomly:"), 1, 137);
                        form.add(TWF12Box, 2, 137);

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
                    try
                    {
                        java.nio.file.Path backupPath = Paths.get("backup", (LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH-mm-ss")) + " " + fileSelector.getSelectionModel().getSelectedItem().getName()));
                        Files.createDirectories(backupPath.getParent());
                        Files.copy(fileSelector.getSelectionModel().getSelectedItem().toPath(), backupPath);
                    }   
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                    byte[] newFileData = Main.buildNewFile(fileSelector.getSelectionModel().getSelectedItem(), units);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(fileSelector.getSelectionModel().getSelectedItem().getName());
                    if(fileSelector.getSelectionModel().getSelectedItem().getName().equals("main.dol") || fileSelector.getSelectionModel().getSelectedItem().getName().equals("Start.dol"))
                    {
                        FileChooser.ExtensionFilter dolFilter = new FileChooser.ExtensionFilter("Gamecube Main Executable File Format (*.dol)", "*.dol");
                        fileChooser.getExtensionFilters().addAll(dolFilter);
                    }
                    else
                    {
                        FileChooser.ExtensionFilter relFilter = new FileChooser.ExtensionFilter("Gamecube Relocatable Executable File Format (*.rel)", "*.rel");
                        fileChooser.getExtensionFilters().addAll(relFilter);
                    }
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
                            successBox.getIcons().add(unitImage);

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

                            test.testUnitData(units);
                        } 
                        catch (IOException ex) 
                        {
                            System.out.println("There was an error creating the output file");
                        }
                    }
                }
            }
        });

        saveFilePresetButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                if(isFileOpened)
                {
                    byte[] presetFileData = null;
                    FileChooser presetFileChooser = new FileChooser();
                    String[] fileName = fileSelector.getSelectionModel().getSelectedItem().getName().split("\\.");

                    presetFileData = Main.buildPresetFile(givenFile, units);
                    presetFileChooser.setInitialFileName(fileName[0]);
                    FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                    presetFileChooser.getExtensionFilters().addAll(bupFilter);
                    presetFileChooser.setTitle("Save As");

                    File dest = presetFileChooser.showSaveDialog(window);
                    if (dest != null) 
                    {
                        try 
                        {
                            FileOutputStream fos = new FileOutputStream(dest);
                            fos.write(presetFileData);
                            fos.close();

                            Stage successBox = new Stage();
                            successBox.setTitle("Export");
                            successBox.getIcons().add(unitImage);

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

                            test.testUnitData(units);
                        } 
                        catch (IOException ex) 
                        {
                            System.out.println("There was an error creating the output file");
                        }
                    }
                }
            }
        });

        loadFilePresetButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                File givenPreset = null;
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select a Preset File");
                FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                fileChooser.getExtensionFilters().addAll(bupFilter);
                givenPreset = fileChooser.showOpenDialog(window);

                byte[] givenPresetData = ByteUtils.readData(givenPreset);

                if(givenPresetData[0] == 0x00)
                {
                    ArrayList<UnitData> presetUnits = Main.getPresetData(givenPreset);

                    for(int i = 0; i < presetUnits.size(); i++)
                    {
                        ArrayList<String> attackNames = new ArrayList<String>();
                        for(int j = 0; j < units.get(i).BattleWeaponData.size(); j++)
                        {
                            attackNames.add(units.get(i).BattleWeaponData.get(j).attackName);
                        }

                        units.get(i).BattleUnitKindData = new ArrayList<BattleUnitKind>();
                        units.get(i).BattleUnitKindPartData = new ArrayList<BattleUnitKindPart>();
                        units.get(i).BattleUnitDefenseData = new ArrayList<BattleUnitDefense>();
                        units.get(i).BattleUnitDefenseAttrData = new ArrayList<BattleUnitDefenseAttr>();
                        units.get(i).StatusVulnerabilityData = new ArrayList<StatusVulnerability>();
                        units.get(i).BattleWeaponData = new ArrayList<BattleWeapon>();

                        for(int j = 0; j < presetUnits.get(i).BattleUnitKindData.size(); j++)
                        {
                            units.get(i).addBattleUnitKindStruct(presetUnits.get(i).BattleUnitKindData.get(j));
                        }
                        for(int j = 0; j < presetUnits.get(i).BattleUnitKindPartData.size(); j++)
                        {
                            units.get(i).addBattleUnitKindPartStruct(presetUnits.get(i).BattleUnitKindPartData.get(j));
                        }
                        for(int j = 0; j < presetUnits.get(i).BattleUnitDefenseData.size(); j++)
                        {
                            units.get(i).addBattleUnitDefenseStruct(presetUnits.get(i).BattleUnitDefenseData.get(j));
                        }
                        for(int j = 0; j < presetUnits.get(i).BattleUnitDefenseAttrData.size(); j++)
                        {
                            units.get(i).addBattleUnitDefenseAttrStruct(presetUnits.get(i).BattleUnitDefenseAttrData.get(j));
                        }
                        for(int j = 0; j < presetUnits.get(i).StatusVulnerabilityData.size(); j++)
                        {
                            units.get(i).addStatusVulnerabilityStruct(presetUnits.get(i).StatusVulnerabilityData.get(j));
                        }
                        for(int j = 0; j < presetUnits.get(i).BattleWeaponData.size(); j++)
                        {
                            units.get(i).addBattleWeaponStruct(presetUnits.get(i).BattleWeaponData.get(j));
                            units.get(i).BattleWeaponData.get(j).attackName = attackNames.get(j);
                        }
                    }

                    loadSelectedUnit(unitSelector.getSelectionModel().getSelectedIndex());
                    
                    centerMenu.getChildren().clear();
                    upperBox.getChildren().clear();
                    upperBox2.getChildren().clear();
                    upperBox.getChildren().add(unitSelector);
                    upperBox2.getChildren().add(new Label(""));
                    
                    showPresetSuccessWindow();
                }
                else
                {
                    showGiveValidFileWindow();
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
                alertBox.getIcons().add(unitImage);

                VBox alertMenu = new VBox();
                alertMenu.setAlignment(Pos.CENTER);
                Text version = new Text("Written by Jemaroo     Version: 2.2.0");
                version.setWrappingWidth(290);
                version.setTextAlignment(TextAlignment.CENTER);
                Text description = new Text("TTYD BattleUnitTool allows you to open up the game's main dol or any rel file containing battle data and edit enemy unit fields.");
                description.setWrappingWidth(290);
                description.setTextAlignment(TextAlignment.CENTER);
                alertMenu.getChildren().addAll(new Label(""), version, new Label(""), description);

                StackPane alertPane = new StackPane();
                alertPane.getChildren().add(alertMenu);
                alertPane.setAlignment(Pos.CENTER);

                Scene alertScene = new Scene(alertPane, 350, 150);

                alertBox.setScene(alertScene);
                alertBox.initModality(Modality.APPLICATION_MODAL);
                alertBox.show();
            }
        });

        window.getIcons().add(unitImage);
        window.show();

        //TODO Change update version
        UpdateChecker updateChecker = new UpdateChecker("Jemaroo", "TTYDBattleUnitTool", "2.2.0");
        updateChecker.check();
        if(updateChecker.isUpdateAvailable())
        {
            Stage updateBox = new Stage();
            updateBox.setTitle("Update Available");
            updateBox.getIcons().add(unitImage);

            Label updateMessage = new Label(updateChecker.getUpdateMessage().get());
            updateMessage.setAlignment(Pos.CENTER);
            updateMessage.setTextAlignment(TextAlignment.CENTER);
            updateMessage.setOnMouseEntered(new EventHandler<MouseEvent>() 
            {
                @Override public void handle(MouseEvent event)
                { 
                    updateMessage.setStyle("-fx-text-fill: blue; -fx-underline: true;");
                }
            });
            updateMessage.setOnMouseExited(new EventHandler<MouseEvent>() 
            {
                @Override public void handle(MouseEvent event)
                { 
                    updateMessage.setStyle("-fx-text-fill: black; -fx-underline: false;");
                }
            });
            updateMessage.setOnMouseClicked(new EventHandler<MouseEvent>() 
            {
                @Override public void handle(MouseEvent event)
                { 
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) 
                    {
                        try 
                        {
                            desktop.browse(new URL("https://github.com/Jemaroo/TTYDBattleUnitTool/releases/latest").toURI());
                        } 
                        catch (Exception e) 
                        {
                            e.printStackTrace();
                        }
                    }
                }
            });

            VBox updateMenu = new VBox();
            updateMenu.setAlignment(Pos.CENTER);
            updateMenu.getChildren().add(updateMessage);

            StackPane alertPane = new StackPane();
            alertPane.getChildren().add(updateMenu);
            alertPane.setAlignment(Pos.CENTER);

            Scene alertScene = new Scene(alertPane, 450, 80);

            updateBox.setScene(alertScene);
            updateBox.initModality(Modality.APPLICATION_MODAL);
            updateBox.show();
        }
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
            statuslist.getItems().clear();
            weaponList.getItems().clear();

            UnitData unit = units.get(index);
            kindList.getItems().setAll(unit.BattleUnitKindData);
            kindList.getItems().addAll(unit.BattleUnitKindPartData);
            defenseList.getItems().setAll(unit.BattleUnitDefenseData);
            defenseList.getItems().addAll(unit.BattleUnitDefenseAttrData);
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
            dangerHPField.setText(String.valueOf(b.dangerHP));
            perilHPField.setText(String.valueOf(b.perilHP));
            levelField.setText(String.valueOf(b.level));
            bonusXPField.setText(String.valueOf(b.bonusXP));
            bonusCoinField.setText(String.valueOf(b.bonusCoin));
            bonusCoinRateField.setText(String.valueOf(b.bonusCoinRate));
            baseCoinField.setText(String.valueOf(b.baseCoin));
            runRateField.setText(String.valueOf(b.runRate));
            pbCapField.setText(String.valueOf(b.pbCap));
            swallowChanceField.setText(String.valueOf(b.swallowChance));
            swallowAttributeSelector.getSelectionModel().select(SAtoIndex(b.swallowAttribute));
            UltraHammerKnockChanceField.setText(String.valueOf(b.ultraHammerKnockChance));
            ItemStealParameterField.setText(String.valueOf(b.itemStealParameter));
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
            ACLevelSelector.getSelectionModel().select(b.ac_level);
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
            TWF2Box.setSelected(b.PreferPartner);
            TWF3Box.setSelected(b.PreferFront);
            TWF4Box.setSelected(b.PreferBack);
            TWF5Box.setSelected(b.PreferSameAlliance);
            TWF6Box.setSelected(b.PreferOppositeAlliance);
            TWF7Box.setSelected(b.PreferLessHealthy);
            TWF8Box.setSelected(b.GreatlyPreferLessHealthy);
            TWF9Box.setSelected(b.PreferLowerHP);
            TWF10Box.setSelected(b.PreferHigherHP);
            TWF11Box.setSelected(b.PreferInPeril);
            TWF12Box.setSelected(b.ChooseWeightedRandomly);
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
        else if (struct instanceof BattleUnitKindPart)
        {
            BattleUnitKindPart b = (BattleUnitKindPart) struct;
            PAF1Box.setSelected(b.MostPreferredSelectTarget);
            PAF2Box.setSelected(b.PreferredSelectTarget);
            PAF3Box.setSelected(b.SelectTarget);
            PAF4Box.setSelected(b.WeakToAttackFxR);
            PAF5Box.setSelected(b.WeakToIcePower);
            PAF6Box.setSelected(b.IsWinged);
            PAF7Box.setSelected(b.IsShelled);
            PAF8Box.setSelected(b.IsBombFlippable);
            PAF9Box.setSelected(b.NeverTargetable);
            PAF10Box.setSelected(b.Untattleable);
            PAF11Box.setSelected(b.JumplikeCannotTarget);
            PAF12Box.setSelected(b.HammerlikeCannotTarget);
            PAF13Box.setSelected(b.ShellTosslikeCannotTarget);
            PAF14Box.setSelected(b.IsImmuneToDamageOrStatus);
            PAF15Box.setSelected(b.IsImmuneToOHKO);
            PAF16Box.setSelected(b.IsImmuneToStatus);
            PCAF1Box.setSelected(b.TopSpiky);
            PCAF2Box.setSelected(b.PreemptiveFrontSpiky);
            PCAF3Box.setSelected(b.FrontSpiky);
            PCAF4Box.setSelected(b.Fiery);
            PCAF5Box.setSelected(b.FieryStatus);
            PCAF6Box.setSelected(b.Icy);
            PCAF7Box.setSelected(b.IcyStatus);
            PCAF8Box.setSelected(b.Poison);
            PCAF9Box.setSelected(b.PoisonStatus);
            PCAF10Box.setSelected(b.Electric);
            PCAF11Box.setSelected(b.ElectricStatus);
            PCAF12Box.setSelected(b.Explosive);
            PCAF13Box.setSelected(b.VolatileExplosive);
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
        else if (statuslist.getSelectionModel().getSelectedItem() != null)
        {
            selected = statuslist.getSelectionModel().getSelectedItem();
        }
        else if (weaponList.getSelectionModel().getSelectedItem() != null)
        {
            selected = weaponList.getSelectionModel().getSelectedItem();
        }

        if (selected instanceof BattleUnitKind b) 
        {
            b.HP = Integer.parseInt(HPField.getText());
            b.dangerHP = Integer.parseInt(dangerHPField.getText());
            b.perilHP = Integer.parseInt(perilHPField.getText());
            b.level = Integer.parseInt(levelField.getText());
            b.bonusXP = Integer.parseInt(bonusXPField.getText());
            b.bonusCoin = Integer.parseInt(bonusCoinField.getText());
            b.bonusCoinRate = Integer.parseInt(bonusCoinRateField.getText());
            b.baseCoin = Integer.parseInt(baseCoinField.getText());
            b.runRate = Integer.parseInt(runRateField.getText());
            b.pbCap = Integer.parseInt(pbCapField.getText());
            b.swallowChance = Integer.parseInt(swallowChanceField.getText());
            b.swallowAttribute = IndexToSA(swallowAttributeSelector.getSelectionModel().getSelectedIndex());
            b.ultraHammerKnockChance = Integer.parseInt(UltraHammerKnockChanceField.getText());
            b.itemStealParameter = Integer.parseInt(ItemStealParameterField.getText());
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
            b.ac_level = ACLevelSelector.getSelectionModel().getSelectedIndex();
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
        else if (selected instanceof BattleUnitKindPart b)
        {
            b.MostPreferredSelectTarget = PAF1Box.isSelected();
            b.PreferredSelectTarget = PAF2Box.isSelected();
            b.SelectTarget = PAF3Box.isSelected();
            b.WeakToAttackFxR = PAF4Box.isSelected();
            b.WeakToIcePower = PAF5Box.isSelected();
            b.IsWinged = PAF6Box.isSelected();
            b.IsShelled = PAF7Box.isSelected();
            b.IsBombFlippable = PAF8Box.isSelected();
            b.NeverTargetable = PAF9Box.isSelected();
            b.Untattleable = PAF10Box.isSelected();
            b.JumplikeCannotTarget = PAF11Box.isSelected();
            b.HammerlikeCannotTarget = PAF12Box.isSelected();
            b.ShellTosslikeCannotTarget = PAF13Box.isSelected();
            b.IsImmuneToDamageOrStatus = PAF14Box.isSelected();
            b.IsImmuneToOHKO = PAF15Box.isSelected();
            b.IsImmuneToStatus = PAF16Box.isSelected();
            b.TopSpiky = PCAF1Box.isSelected();
            b.PreemptiveFrontSpiky = PCAF2Box.isSelected();
            b.FrontSpiky = PCAF3Box.isSelected();
            b.Fiery = PCAF4Box.isSelected();
            b.FieryStatus = PCAF5Box.isSelected();
            b.Icy = PCAF6Box.isSelected();
            b.IcyStatus = PCAF7Box.isSelected();
            b.Poison = PCAF8Box.isSelected();
            b.PoisonStatus = PCAF9Box.isSelected();
            b.Electric = PCAF10Box.isSelected();
            b.ElectricStatus = PCAF11Box.isSelected();
            b.Explosive = PCAF12Box.isSelected();
            b.VolatileExplosive = PCAF13Box.isSelected();
        }
    }

    /**
     * @Author Jemaroo
     * @Function Saves the preset fields into the variables
     */
    private void savePresetFieldsToSelectedStruct(Object preset) 
    {
        if (preset instanceof BattleUnitKind b) 
        {
            
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).HP = b.HP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).dangerHP = b.dangerHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).perilHP = b.perilHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).level = b.level;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).bonusXP = b.bonusXP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).bonusCoin = b.bonusCoin;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).bonusCoinRate = b.bonusCoinRate;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).baseCoin = b.baseCoin;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).runRate = b.runRate;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).pbCap = b.pbCap;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).swallowChance = b.swallowChance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).swallowAttribute = b.swallowAttribute;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).ultraHammerKnockChance = b.ultraHammerKnockChance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).itemStealParameter = b.itemStealParameter;
        } 
        else if (preset instanceof BattleUnitDefense b) 
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).normal = b.normal;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).fire = b.fire;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).ice = b.ice;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).explosion = b.explosion;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).electric = b.electric;
        }
        else if (preset instanceof BattleUnitDefenseAttr b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).normal = b.normal;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).fire = b.fire;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).ice = b.ice;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).explosion = b.explosion;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).electric = b.electric;
        }
        else if (preset instanceof StatusVulnerability b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).sleep = b.sleep;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).stop = b.stop;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).dizzy = b.dizzy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).poison = b.poison;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).confuse = b.confuse;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).electric = b.electric;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).burn = b.burn;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).freeze = b.freeze;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).huge = b.huge;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).tiny = b.tiny;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).attack_up = b.attack_up;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).attack_down = b.attack_down;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).defense_up = b.defense_up;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).defense_down = b.defense_down;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).allergic = b.allergic;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).fright = b.fright;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).gale_force = b.gale_force;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).fast = b.fast;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).slow = b.slow;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).dodgy = b.dodgy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).invisible = b.invisible;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(statuslist.getSelectionModel().getSelectedIndex()).ohko = b.ohko;
        }
        else if (preset instanceof BattleWeapon b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).accuracy = b.accuracy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fp_cost = b.fp_cost;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sp_cost = b.sp_cost;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).superguard_state = b.superguard_state;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sylish_multiplier = b.sylish_multiplier;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).bingo_slot_inc_chance = b.bingo_slot_inc_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage1 = b.base_damage1;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage2 = b.base_damage2;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage3 = b.base_damage3;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage4 = b.base_damage4;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage5 = b.base_damage5;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage6 = b.base_damage6;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage7 = b.base_damage7;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage8 = b.base_damage8;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage1 = b.base_fpdamage1;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage2 = b.base_fpdamage2;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage3 = b.base_fpdamage3;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage4 = b.base_fpdamage4;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage5 = b.base_fpdamage5;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage6 = b.base_fpdamage6;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage7 = b.base_fpdamage7;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage8 = b.base_fpdamage8;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetMarioOrShellShield = b.CannotTargetMarioOrShellShield;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetPartner = b.CannotTargetPartner;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetEnemy = b.CannotTargetEnemy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetOppositeAlliance = b.CannotTargetOppositeAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetOwnAlliance = b.CannotTargetOwnAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetSelf = b.CannotTargetSelf;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetSameSpecies = b.CannotTargetSameSpecies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).OnlyTargetSelf = b.OnlyTargetSelf;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).OnlyTargetMario = b.OnlyTargetMario;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).SingleTarget = b.SingleTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).MultipleTarget = b.MultipleTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Tattlelike = b.Tattlelike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetCeiling = b.CannotTargetCeiling;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetFloating = b.CannotTargetFloating;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetGrounded = b.CannotTargetGrounded;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Jumplike = b.Jumplike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Hammerlike = b.Hammerlike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ShellTosslike = b.ShellTosslike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).RecoilDamage = b.RecoilDamage;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CanOnlyTargetFrontmost = b.CanOnlyTargetFrontmost;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).TargetSameAllianceDirection = b.TargetSameAllianceDirection;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).TargetOppositeAllianceDirection = b.TargetOppositeAllianceDirection;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).element = b.element;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).damage_pattern = b.damage_pattern;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ac_level = b.ac_level;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).BadgeCanAffectPower = b.BadgeCanAffectPower;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).StatusCanAffectPower = b.StatusCanAffectPower;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).IsChargeable = b.IsChargeable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotMiss = b.CannotMiss;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).DiminishingReturnsByHit = b.DiminishingReturnsByHit;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).DiminishingReturnsByTarget = b.DiminishingReturnsByTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PiercesDefense = b.PiercesDefense;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).IgnoreTargetStatusVulnerability = b.IgnoreTargetStatusVulnerability;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).IgnitesIfBurned = b.IgnitesIfBurned;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).FlipsShellEnemies = b.FlipsShellEnemies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).FlipsBombFlippableEnemies = b.FlipsBombFlippableEnemies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).GroundsWingedEnemies = b.GroundsWingedEnemies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CanBeUsedAsConfusedAction = b.CanBeUsedAsConfusedAction;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Unguardable = b.Unguardable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Electric = b.Electric;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).TopSpiky = b.TopSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreemptiveFrontSpiky = b.PreemptiveFrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).FrontSpiky = b.FrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Fiery = b.Fiery;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Icy = b.Icy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Poison = b.Poison;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Explosive = b.Explosive;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).VolatileExplosive = b.VolatileExplosive;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Payback = b.Payback;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).HoldFast = b.HoldFast;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferMario = b.PreferMario;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferPartner = b.PreferPartner;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferFront = b.PreferFront;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferBack = b.PreferBack;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferSameAlliance = b.PreferSameAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferOppositeAlliance = b.PreferOppositeAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferLessHealthy = b.PreferLessHealthy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).GreatlyPreferLessHealthy = b.GreatlyPreferLessHealthy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferLowerHP = b.PreferLowerHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferHigherHP = b.PreferHigherHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferInPeril = b.PreferInPeril;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ChooseWeightedRandomly = b.ChooseWeightedRandomly;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sleep_chance = b.sleep_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sleep_time = b.sleep_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stop_chance = b.stop_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stop_time = b.stop_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dizzy_chance = b.dizzy_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dizzy_time = b.dizzy_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).poison_chance = b.poison_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).poison_time = b.poison_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).poison_strength = b.poison_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).confuse_chance = b.confuse_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).confuse_time = b.confuse_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).electric_chance = b.electric_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).electric_time = b.electric_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dodgy_chance = b.dodgy_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dodgy_time = b.dodgy_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).burn_chance = b.burn_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).burn_time = b.burn_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).freeze_chance = b.freeze_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).freeze_time = b.freeze_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).size_change_chance = b.size_change_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).size_change_time = b.size_change_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).size_change_strength = b.size_change_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).atk_change_chance = b.atk_change_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).atk_change_time = b.atk_change_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).atk_change_strength = b.atk_change_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).def_change_chance = b.def_change_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).def_change_time = b.def_change_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).def_change_strength = b.def_change_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).allergic_chance = b.allergic_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).allergic_time = b.allergic_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ohko_chance = b.ohko_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).charge_strength = b.charge_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fast_chance = b.fast_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fast_time = b.fast_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).slow_chance = b.slow_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).slow_time = b.slow_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fright_chance = b.fright_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).gale_force_chance = b.gale_force_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).payback_time = b.payback_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).hold_fast_time = b.hold_fast_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).invisible_chance = b.invisible_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).invisible_time = b.invisible_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).hp_regen_time = b.hp_regen_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).hp_regen_strength = b.hp_regen_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fp_regen_time = b.fp_regen_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fp_regen_strength = b.fp_regen_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight1 = b.stage_background_fallweight1;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight2 = b.stage_background_fallweight2;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight3 = b.stage_background_fallweight3;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight4 = b.stage_background_fallweight4;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_nozzle_turn_chance = b.stage_nozzle_turn_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_nozzle_fire_chance = b.stage_nozzle_fire_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_ceiling_fall_chance = b.stage_ceiling_fall_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_object_fall_chance = b.stage_object_fall_chance;
        }
        else if (preset instanceof BattleUnitKindPart b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).MostPreferredSelectTarget = b.MostPreferredSelectTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).PreferredSelectTarget = b.PreferredSelectTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).SelectTarget = b.SelectTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).WeakToAttackFxR = b.WeakToAttackFxR;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).WeakToIcePower = b.WeakToIcePower;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsWinged = b.IsWinged;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsShelled = b.IsShelled;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsBombFlippable = b.IsBombFlippable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).NeverTargetable = b.NeverTargetable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Untattleable = b.Untattleable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).JumplikeCannotTarget = b.JumplikeCannotTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).HammerlikeCannotTarget = b.HammerlikeCannotTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).ShellTosslikeCannotTarget = b.ShellTosslikeCannotTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsImmuneToDamageOrStatus = b.IsImmuneToDamageOrStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsImmuneToOHKO = b.IsImmuneToOHKO;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsImmuneToStatus = b.IsImmuneToStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).TopSpiky = b.TopSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).PreemptiveFrontSpiky = b.PreemptiveFrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).FrontSpiky = b.FrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Fiery = b.Fiery;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).FieryStatus = b.FieryStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Icy = b.Icy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IcyStatus = b.IcyStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Poison = b.Poison;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).PoisonStatus = b.PoisonStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Electric = b.Electric;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).ElectricStatus = b.ElectricStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Explosive = b.Explosive;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).VolatileExplosive = b.VolatileExplosive;
        }
    }

    public void showGiveValidFileWindow()
    {
        Stage errorBox = new Stage();
        errorBox.setTitle("Error");
        errorBox.getIcons().add(unitImage);

        VBox errorMenu = new VBox();
        Text message = new Text("Please Give a Valid .bup File for the Given Type You're Trying to Replace.");
        message.setWrappingWidth(210);
        message.setTextAlignment(TextAlignment.CENTER);
        errorMenu.setAlignment(Pos.CENTER);
        errorMenu.getChildren().add(message);

        StackPane successPane = new StackPane();
        successPane.getChildren().add(errorMenu);
        successPane.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(successPane, 220, 50);

        errorBox.setScene(successScene);
        errorBox.initModality(Modality.APPLICATION_MODAL);
        errorBox.show();
    }

    public void showPresetSuccessWindow()
    {
        Stage successBox = new Stage();
        successBox.setTitle("Success");
        successBox.getIcons().add(unitImage);

        VBox successMenu = new VBox();
        Text message = new Text("Successfully Imported!");
        message.setWrappingWidth(290);
        message.setTextAlignment(TextAlignment.CENTER);
        successMenu.setAlignment(Pos.CENTER);
        successMenu.getChildren().add(message);

        StackPane successPane = new StackPane();
        successPane.getChildren().add(successMenu);
        successPane.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(successPane, 150, 50);

        successBox.setScene(successScene);
        successBox.initModality(Modality.APPLICATION_MODAL);
        successBox.show();
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Swallow Attribute into it's index
     */
    public static int SAtoIndex(int SA)
    {
        switch(SA)
        {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 4: return 3;
            case 8: return 4;
            default: return 0;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a index into it's Swallow Attribute
     */
    public static int IndexToSA(int index)
    {
        switch(index)
        {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 4: return 8;
            default: return 0;
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

    public void setRed0TextFieldFormats()
    {
        HPField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {HPField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {HPField.setStyle("-fx-text-fill: black;");}});
        dangerHPField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {dangerHPField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {dangerHPField.setStyle("-fx-text-fill: black;");}});
        perilHPField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {perilHPField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {perilHPField.setStyle("-fx-text-fill: black;");}});
        levelField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {levelField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {levelField.setStyle("-fx-text-fill: black;");}});
        bonusXPField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {bonusXPField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {bonusXPField.setStyle("-fx-text-fill: black;");}});
        bonusCoinField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {bonusCoinField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {bonusCoinField.setStyle("-fx-text-fill: black;");}});
        bonusCoinRateField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {bonusCoinRateField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {bonusCoinRateField.setStyle("-fx-text-fill: black;");}});
        baseCoinField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {baseCoinField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {baseCoinField.setStyle("-fx-text-fill: black;");}});
        runRateField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {runRateField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {runRateField.setStyle("-fx-text-fill: black;");}});
        pbCapField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {pbCapField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {pbCapField.setStyle("-fx-text-fill: black;");}});
        swallowChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {swallowChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {swallowChanceField.setStyle("-fx-text-fill: black;");}});
        UltraHammerKnockChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {UltraHammerKnockChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {UltraHammerKnockChanceField.setStyle("-fx-text-fill: black;");}});
        ItemStealParameterField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ItemStealParameterField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ItemStealParameterField.setStyle("-fx-text-fill: black;");}});
        NormalField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {NormalField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {NormalField.setStyle("-fx-text-fill: black;");}});
        FireField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FireField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FireField.setStyle("-fx-text-fill: black;");}});
        IceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {IceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {IceField.setStyle("-fx-text-fill: black;");}});
        ExplosionField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ExplosionField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ExplosionField.setStyle("-fx-text-fill: black;");}});
        ElectricField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ElectricField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ElectricField.setStyle("-fx-text-fill: black;");}});
        SleepField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SleepField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SleepField.setStyle("-fx-text-fill: black;");}});
        StopField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {StopField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {StopField.setStyle("-fx-text-fill: black;");}});
        DizzyField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DizzyField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DizzyField.setStyle("-fx-text-fill: black;");}});
        PoisonField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {PoisonField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {PoisonField.setStyle("-fx-text-fill: black;");}});
        ConfuseField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ConfuseField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ConfuseField.setStyle("-fx-text-fill: black;");}});
        BurnField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BurnField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BurnField.setStyle("-fx-text-fill: black;");}});
        FreezeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FreezeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FreezeField.setStyle("-fx-text-fill: black;");}});
        HugeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {HugeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {HugeField.setStyle("-fx-text-fill: black;");}});
        TinyField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {TinyField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {TinyField.setStyle("-fx-text-fill: black;");}});
        AttackUpField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AttackUpField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AttackUpField.setStyle("-fx-text-fill: black;");}});
        AttackDownField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AttackDownField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AttackDownField.setStyle("-fx-text-fill: black;");}});
        DefenseUpField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DefenseUpField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DefenseUpField.setStyle("-fx-text-fill: black;");}});
        DefenseDownField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DefenseDownField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DefenseDownField.setStyle("-fx-text-fill: black;");}});
        AllergicField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AllergicField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AllergicField.setStyle("-fx-text-fill: black;");}});
        FrightField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FrightField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FrightField.setStyle("-fx-text-fill: black;");}});
        GaleForceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {GaleForceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {GaleForceField.setStyle("-fx-text-fill: black;");}});
        FastField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FastField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FastField.setStyle("-fx-text-fill: black;");}});
        SlowField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SlowField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SlowField.setStyle("-fx-text-fill: black;");}});
        DodgyField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DodgyField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DodgyField.setStyle("-fx-text-fill: black;");}});
        InvisibleField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {InvisibleField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {InvisibleField.setStyle("-fx-text-fill: black;");}});
        OHKOField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {OHKOField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {OHKOField.setStyle("-fx-text-fill: black;");}});
        AccuracyField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AccuracyField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AccuracyField.setStyle("-fx-text-fill: black;");}});
        FPCostField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FPCostField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FPCostField.setStyle("-fx-text-fill: black;");}});
        SPCostField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SPCostField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SPCostField.setStyle("-fx-text-fill: black;");}});
        StylishMultiplierField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {StylishMultiplierField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {StylishMultiplierField.setStyle("-fx-text-fill: black;");}});
        BingoSlotIncChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BingoSlotIncChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BingoSlotIncChanceField.setStyle("-fx-text-fill: black;");}});
        BaseDamage1Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage1Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage1Field.setStyle("-fx-text-fill: black;");}});
        BaseDamage2Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage2Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage2Field.setStyle("-fx-text-fill: black;");}});
        BaseDamage3Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage3Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage3Field.setStyle("-fx-text-fill: black;");}});
        BaseDamage4Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage4Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage4Field.setStyle("-fx-text-fill: black;");}});
        BaseDamage5Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage5Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage5Field.setStyle("-fx-text-fill: black;");}});
        BaseDamage6Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage6Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage6Field.setStyle("-fx-text-fill: black;");}});
        BaseDamage7Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage7Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage7Field.setStyle("-fx-text-fill: black;");}});
        BaseDamage8Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseDamage8Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseDamage8Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage1Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage1Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage1Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage2Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage2Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage2Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage3Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage3Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage3Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage4Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage4Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage4Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage5Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage5Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage5Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage6Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage6Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage6Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage7Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage7Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage7Field.setStyle("-fx-text-fill: black;");}});
        BaseFPDamage8Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BaseFPDamage8Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BaseFPDamage8Field.setStyle("-fx-text-fill: black;");}});
        SleepChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SleepChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SleepChanceField.setStyle("-fx-text-fill: black;");}});
        SleepTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SleepTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SleepTimeField.setStyle("-fx-text-fill: black;");}});
        StopChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {StopChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {StopChanceField.setStyle("-fx-text-fill: black;");}});
        StopTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {StopTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {StopTimeField.setStyle("-fx-text-fill: black;");}});
        DizzyChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DizzyChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DizzyChanceField.setStyle("-fx-text-fill: black;");}});
        DizzyTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DizzyTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DizzyTimeField.setStyle("-fx-text-fill: black;");}});
        PoisonChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {PoisonChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {PoisonChanceField.setStyle("-fx-text-fill: black;");}});
        PoisonTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {PoisonTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {PoisonTimeField.setStyle("-fx-text-fill: black;");}});
        PoisonStrengthField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {PoisonStrengthField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {PoisonStrengthField.setStyle("-fx-text-fill: black;");}});
        ConfuseChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ConfuseChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ConfuseChanceField.setStyle("-fx-text-fill: black;");}});
        ConfuseTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ConfuseTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ConfuseTimeField.setStyle("-fx-text-fill: black;");}});
        ElectricChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ElectricChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ElectricChanceField.setStyle("-fx-text-fill: black;");}});
        ElectricTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ElectricTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ElectricTimeField.setStyle("-fx-text-fill: black;");}});
        DodgyChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DodgyChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DodgyChanceField.setStyle("-fx-text-fill: black;");}});
        DodgyTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DodgyTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DodgyTimeField.setStyle("-fx-text-fill: black;");}});
        BurnChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BurnChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BurnChanceField.setStyle("-fx-text-fill: black;");}});
        BurnTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {BurnTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {BurnTimeField.setStyle("-fx-text-fill: black;");}});
        FreezeChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FreezeChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FreezeChanceField.setStyle("-fx-text-fill: black;");}});
        FreezeTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FreezeTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FreezeTimeField.setStyle("-fx-text-fill: black;");}});
        SizeChangeChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SizeChangeChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SizeChangeChanceField.setStyle("-fx-text-fill: black;");}});
        SizeChangeTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SizeChangeTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SizeChangeTimeField.setStyle("-fx-text-fill: black;");}});
        SizeChangeStrengthField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SizeChangeStrengthField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SizeChangeStrengthField.setStyle("-fx-text-fill: black;");}});
        AtkChangeChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AtkChangeChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AtkChangeChanceField.setStyle("-fx-text-fill: black;");}});
        AtkChangeTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AtkChangeTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AtkChangeTimeField.setStyle("-fx-text-fill: black;");}});
        AtkChangeStrengthField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AtkChangeStrengthField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AtkChangeStrengthField.setStyle("-fx-text-fill: black;");}});
        DefChangeChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DefChangeChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DefChangeChanceField.setStyle("-fx-text-fill: black;");}});
        DefChangeTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DefChangeTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DefChangeTimeField.setStyle("-fx-text-fill: black;");}});
        DefChangeStrengthField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {DefChangeStrengthField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {DefChangeStrengthField.setStyle("-fx-text-fill: black;");}});
        AllergicChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AllergicChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AllergicChanceField.setStyle("-fx-text-fill: black;");}});
        AllergicTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {AllergicTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {AllergicTimeField.setStyle("-fx-text-fill: black;");}});
        OHKOChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {OHKOChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {OHKOChanceField.setStyle("-fx-text-fill: black;");}});
        ChargeStrengthField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {ChargeStrengthField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {ChargeStrengthField.setStyle("-fx-text-fill: black;");}});
        FastChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FastChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FastChanceField.setStyle("-fx-text-fill: black;");}});
        FastTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FastTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FastTimeField.setStyle("-fx-text-fill: black;");}});
        SlowChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SlowChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SlowChanceField.setStyle("-fx-text-fill: black;");}});
        SlowTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {SlowTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {SlowTimeField.setStyle("-fx-text-fill: black;");}});
        FrightChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FrightChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FrightChanceField.setStyle("-fx-text-fill: black;");}});
        GaleForceChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {GaleForceChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {GaleForceChanceField.setStyle("-fx-text-fill: black;");}});
        PaybackTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {PaybackTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {PaybackTimeField.setStyle("-fx-text-fill: black;");}});
        HoldFastTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {HoldFastTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {HoldFastTimeField.setStyle("-fx-text-fill: black;");}});
        InvisibleChanceField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {InvisibleChanceField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {InvisibleChanceField.setStyle("-fx-text-fill: black;");}});
        InvisibleTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {InvisibleTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {InvisibleTimeField.setStyle("-fx-text-fill: black;");}});
        HPRegenTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {HPRegenTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {HPRegenTimeField.setStyle("-fx-text-fill: black;");}});
        HPRegenStrengthField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {HPRegenStrengthField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {HPRegenStrengthField.setStyle("-fx-text-fill: black;");}});
        FPRegenTimeField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FPRegenTimeField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FPRegenTimeField.setStyle("-fx-text-fill: black;");}});
        FPRegenStrengthField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {FPRegenStrengthField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {FPRegenStrengthField.setStyle("-fx-text-fill: black;");}});
        STGFall1Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGFall1Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGFall1Field.setStyle("-fx-text-fill: black;");}});
        STGFall2Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGFall2Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGFall2Field.setStyle("-fx-text-fill: black;");}});
        STGFall3Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGFall3Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGFall3Field.setStyle("-fx-text-fill: black;");}});
        STGFall4Field.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGFall4Field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGFall4Field.setStyle("-fx-text-fill: black;");}});
        STGNozzleTurnField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGNozzleTurnField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGNozzleTurnField.setStyle("-fx-text-fill: black;");}});
        STGNozzleFireField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGNozzleFireField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGNozzleFireField.setStyle("-fx-text-fill: black;");}});
        STGCeilingFallField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGCeilingFallField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGCeilingFallField.setStyle("-fx-text-fill: black;");}});
        STGObjectFallField.textProperty().addListener((obs, oldText, newText) -> { if ("0".equals(newText)) {STGObjectFallField.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");} else {STGObjectFallField.setStyle("-fx-text-fill: black;");}});
    }

    public ImageView unitImageSelector(String name)
    {
        ImageView retIV = new ImageView(unknownImage);
        retIV.setFitHeight(20); retIV.setFitWidth(20);

        if(name.equals("Goomba")){retIV.setImage(unitGoombaImage); return retIV;}
        if(name.equals("Paragoomba")){retIV.setImage(unitParagoombaImage); return retIV;}
        if(name.equals("Spiky Goomba")){retIV.setImage(unitSpikyGoombaImage); return retIV;}
        if(name.equals("Hyper Goomba")){retIV.setImage(unitHyperGoombaImage); return retIV;}
        if(name.equals("Hyper Paragoomba")){retIV.setImage(unitHyperParagoombaImage); return retIV;}
        if(name.equals("Hyper Spiky Goomba")){retIV.setImage(unitHyperSpikyGoombaImage); return retIV;}
        if(name.equals("Gloomba")){retIV.setImage(unitGloombaImage); return retIV;}
        if(name.equals("Paragloomba")){retIV.setImage(unitParagloombaImage); return retIV;}
        if(name.equals("Spiky Gloomba")){retIV.setImage(unitSpikyGloombaImage); return retIV;}
        if(name.equals("Koopa Troopa")){retIV.setImage(unitKoopaTroopaImage); return retIV;}
        if(name.equals("Paratroopa")){retIV.setImage(unitParatroopaImage); return retIV;}
        if(name.equals("KP Koopa")){retIV.setImage(unitKPKoopaImage); return retIV;}
        if(name.equals("KP Paratroopa")){retIV.setImage(unitKPParatroopaImage); return retIV;}
        if(name.equals("Shady Koopa")){retIV.setImage(unitShadyKoopaImage); return retIV;}
        if(name.equals("Shady Paratroopa")){retIV.setImage(unitShadyParatroopaImage); return retIV;}
        if(name.equals("Dark Koopa")){retIV.setImage(unitDarkKoopaImage); return retIV;}
        if(name.equals("Dark Paratroopa")){retIV.setImage(unitDarkParatroopaImage); return retIV;}
        if(name.equals("Koopatrol")){retIV.setImage(unitKoopatrolImage); return retIV;}
        if(name.equals("Dark Koopatrol")){retIV.setImage(unitDarkKoopatrolImage); return retIV;}
        if(name.equals("Dull Bones")){retIV.setImage(unitDullBonesImage); return retIV;}
        if(name.equals("Red Bones")){retIV.setImage(unitRedBonesImage); return retIV;}
        if(name.equals("Dry Bones")){retIV.setImage(unitDryBonesImage); return retIV;}
        if(name.equals("Dark Bones")){retIV.setImage(unitDarkBonesImage); return retIV;}
        if(name.equals("Hammer Bro")){retIV.setImage(unitHammerBroImage); return retIV;}
        if(name.equals("Boomerang Bro")){retIV.setImage(unitBoomerangBroImage); return retIV;}
        if(name.equals("Fire Bro")){retIV.setImage(unitFireBroImage); return retIV;}
        if(name.equals("Lakitu")){retIV.setImage(unitLakituImage); return retIV;}
        if(name.equals("Dark Lakitu")){retIV.setImage(unitDarkLakituImage); return retIV;}
        if(name.equals("Spiny")){retIV.setImage(unitSpinyImage); return retIV;}
        if(name.equals("Sky Blue Spiny")){retIV.setImage(unitSkyBlueSpinyImage); return retIV;}
        if(name.equals("Buzzy Beetle")){retIV.setImage(unitBuzzyBeetleImage); return retIV;}
        if(name.equals("Spiky Buzzy")){retIV.setImage(unitSpikyBuzzyImage); return retIV;}
        if(name.equals("Parabuzzy")){retIV.setImage(unitParabuzzyImage); return retIV;}
        if(name.equals("Spiky Parabuzzy")){retIV.setImage(unitSpikyParabuzzyImage); return retIV;}
        if(name.equals("Red Spiky Buzzy")){retIV.setImage(unitRedSpikyBuzzyImage); return retIV;}
        if(name.equals("Magikoopa")){retIV.setImage(unitMagikoopaImage); return retIV;}
        if(name.equals("Red Magikoopa")){retIV.setImage(unitRedMagikoopaImage); return retIV;}
        if(name.equals("White Magikoopa")){retIV.setImage(unitWhiteMagikoopaImage); return retIV;}
        if(name.equals("Green Magikoopa")){retIV.setImage(unitGreenMagikoopaImage); return retIV;}
        if(name.equals("Kammy Koopa")){retIV.setImage(unitKammyKoopaImage); return retIV;}
        if(name.equals("Bowser")){retIV.setImage(unitBowserImage); return retIV;}
        if(name.equals("Gus")){retIV.setImage(unitGusImage); return retIV;}
        if(name.equals("Dark Craw")){retIV.setImage(unitDarkCrawImage); return retIV;}
        if(name.equals("Bandit")){retIV.setImage(unitBanditImage); return retIV;}
        if(name.equals("Big Bandit")){retIV.setImage(unitBigBanditImage); return retIV;}
        if(name.equals("Badge Bandit")){retIV.setImage(unitBadgeBanditImage); return retIV;}
        if(name.equals("Spinia")){retIV.setImage(unitSpiniaImage); return retIV;}
        if(name.equals("Spania")){retIV.setImage(unitSpaniaImage); return retIV;}
        if(name.equals("Spunia")){retIV.setImage(unitSpuniaImage); return retIV;}
        if(name.equals("Fuzzy")){retIV.setImage(unitFuzzyImage); return retIV;}
        if(name.equals("Fuzzy Horde")){retIV.setImage(unitFuzzyImage); return retIV;}
        if(name.equals("Gold Fuzzy")){retIV.setImage(unitGoldFuzzyImage); return retIV;}
        if(name.equals("Green Fuzzy")){retIV.setImage(unitGreenFuzzyImage); return retIV;}
        if(name.equals("Flower Fuzzy")){retIV.setImage(unitFlowerFuzzyImage); return retIV;}
        if(name.equals("Pokey")){retIV.setImage(unitPokeyImage); return retIV;}
        if(name.equals("Poison Pokey")){retIV.setImage(unitPoisonPokeyImage); return retIV;}
        if(name.equals("Pale Piranha")){retIV.setImage(unitPalePiranhaImage); return retIV;}
        if(name.equals("Putrid Piranha")){retIV.setImage(unitPutridPiranhaImage); return retIV;}
        if(name.equals("Frost Piranha")){retIV.setImage(unitFrostPiranhaImage); return retIV;}
        if(name.equals("Piranha Plant")){retIV.setImage(unitPiranhaPlantImage); return retIV;}
        if(name.equals("Crazee Dayzee")){retIV.setImage(unitCrazeeDayzeeImage); return retIV;}
        if(name.equals("Amazy Dayzee")){retIV.setImage(unitAmazyDayzeeImage); return retIV;}
        if(name.equals("Pider")){retIV.setImage(unitPiderImage); return retIV;}
        if(name.equals("Arantula")){retIV.setImage(unitArantulaImage); return retIV;}
        if(name.equals("Swooper")){retIV.setImage(unitSwooperImage); return retIV;}
        if(name.equals("Swoopula")){retIV.setImage(unitSwoopulaImage); return retIV;}
        if(name.equals("Swampire")){retIV.setImage(unitSwampireImage); return retIV;}
        if(name.equals("Dark Puff")){retIV.setImage(unitDarkPuffImage); return retIV;}
        if(name.equals("Ruff Puff")){retIV.setImage(unitRuffPuffImage); return retIV;}
        if(name.equals("Ice Puff")){retIV.setImage(unitIcePuffImage); return retIV;}
        if(name.equals("Poison Puff")){retIV.setImage(unitPoisonPuffImage); return retIV;}
        if(name.equals("Boo")){retIV.setImage(unitBooImage); return retIV;}
        if(name.equals("Atomic Boo")){retIV.setImage(unitAtomicBooImage); return retIV;}
        if(name.equals("Dark Boo")){retIV.setImage(unitDarkBooImage); return retIV;}
        if(name.equals("Ember")){retIV.setImage(unitEmberImage); return retIV;}
        if(name.equals("Lava Bubble")){retIV.setImage(unitLavaBubbleImage); return retIV;}
        if(name.equals("Phantom Ember")){retIV.setImage(unitPhantomEmberImage); return retIV;}
        if(name.equals("Bald Cleft")){retIV.setImage(unitBaldCleftImage); return retIV;}
        if(name.equals("Hyper Bald Cleft")){retIV.setImage(unitHyperBaldCleftImage); return retIV;}
        if(name.equals("Cleft")){retIV.setImage(unitCleftImage); return retIV;}
        if(name.equals("Iron Cleft 1")){retIV.setImage(unitIronCleft1Image); return retIV;}
        if(name.equals("Iron Cleft 2")){retIV.setImage(unitIronCleft2Image); return retIV;}
        if(name.equals("Hyper Cleft")){retIV.setImage(unitHyperCleftImage); return retIV;}
        if(name.equals("Moon Cleft")){retIV.setImage(unitMoonCleftImage); return retIV;}
        if(name.equals("Bristle")){retIV.setImage(unitBristleImage); return retIV;}
        if(name.equals("Dark Bristle")){retIV.setImage(unitDarkBristleImage); return retIV;}
        if(name.equals("Bob-omb")){retIV.setImage(unitBobombImage); return retIV;}
        if(name.equals("Bulky Bob-omb")){retIV.setImage(unitBulkyBobombImage); return retIV;}
        if(name.equals("Bob-ulk")){retIV.setImage(unitBobulkImage); return retIV;}
        if(name.equals("Chain-Chomp")){retIV.setImage(unitChainChompImage); return retIV;}
        if(name.equals("Red Chomp")){retIV.setImage(unitRedChompImage); return retIV;}
        if(name.equals("Bill Blaster")){retIV.setImage(unitBillBlasterImage); return retIV;}
        if(name.equals("Bullet Bill")){retIV.setImage(unitBulletBillImage); return retIV;}
        if(name.equals("Bombshell Bill Blaster")){retIV.setImage(unitBombshellBillBlasterImage); return retIV;}
        if(name.equals("Bombshell Bill")){retIV.setImage(unitBombshellBillImage); return retIV;}
        if(name.equals("Dark Wizzerd")){retIV.setImage(unitDarkWizzerdImage); return retIV;}
        if(name.equals("Wizzerd")){retIV.setImage(unitWizzerdImage); return retIV;}
        if(name.equals("Elite Wizzerd")){retIV.setImage(unitEliteWizzerdImage); return retIV;}
        if(name.equals("Blooper")){retIV.setImage(unitBlooperImage); return retIV;}
        if(name.equals("Blooper Right Tentacle")){retIV.setImage(unitBlooperImage); return retIV;}
        if(name.equals("Blooper Left Tentacle")){retIV.setImage(unitBlooperImage); return retIV;}
        if(name.equals("Hooktail")){retIV.setImage(unitHooktailImage); return retIV;}
        if(name.equals("Gloomtail")){retIV.setImage(unitGloomtailImage); return retIV;}
        if(name.equals("Bonetail")){retIV.setImage(unitBonetailImage); return retIV;}
        if(name.equals("Rawk Hawk")){retIV.setImage(unitRawkHawkImage); return retIV;}
        if(name.equals("Macho Grubba")){retIV.setImage(unitMachoGrubbaImage); return retIV;}
        if(name.equals("Doopliss")){retIV.setImage(unitDooplissImage); return retIV;}
        if(name.equals("Doopliss Mario")){retIV.setImage(unitDooplissMarioImage); return retIV;}
        if(name.equals("Cortez")){retIV.setImage(unitCortezImage); return retIV;}
        if(name.equals("Bone Pile")){retIV.setImage(unitCortezBonePileImage); return retIV;}
        if(name.equals("Cortez Hook")){retIV.setImage(unitCortezHookImage); return retIV;}
        if(name.equals("Cortez Rapier")){retIV.setImage(unitCortezRapierImage); return retIV;}
        if(name.equals("Cortez Saber")){retIV.setImage(unitCortezSaberImage); return retIV;}
        if(name.equals("Cortez Sword")){retIV.setImage(unitCortezSwordImage); return retIV;}
        if(name.equals("Smorg")){retIV.setImage(unitSmorgImage); return retIV;}
        if(name.equals("Smorg Tentacle 1")){retIV.setImage(unitSmorgImage); return retIV;}
        if(name.equals("Smorg Tentacle 2")){retIV.setImage(unitSmorgImage); return retIV;}
        if(name.equals("Smorg Tentacle 3")){retIV.setImage(unitSmorgImage); return retIV;}
        if(name.equals("Smorg Claw")){retIV.setImage(unitSmorgImage); return retIV;}
        if(name.equals("X-Naut")){retIV.setImage(unitXNautImage); return retIV;}
        if(name.equals("X-Naut Platoon Formation 1")){retIV.setImage(unitXNautImage); return retIV;}
        if(name.equals("X-Naut Platoon Formation 2")){retIV.setImage(unitXNautImage); return retIV;}
        if(name.equals("X-Naut Platoon Formation 3")){retIV.setImage(unitXNautImage); return retIV;}
        if(name.equals("X-Naut PHD")){retIV.setImage(unitXNautPHDImage); return retIV;}
        if(name.equals("Elite X-Naut")){retIV.setImage(unitEliteXNautImage); return retIV;}
        if(name.equals("Yux")){retIV.setImage(unitYuxImage); return retIV;}
        if(name.equals("Mini-Yux")){retIV.setImage(unitMiniYuxImage); return retIV;}
        if(name.equals("Z-Yux")){retIV.setImage(unitZYuxImage); return retIV;}
        if(name.equals("Mini-Z-Yux")){retIV.setImage(unitMiniZYuxImage); return retIV;}
        if(name.equals("X-Yux")){retIV.setImage(unitXYuxImage); return retIV;}
        if(name.equals("Mini-X-Yux")){retIV.setImage(unitMiniXYuxImage); return retIV;}
        if(name.equals("Grodus X")){retIV.setImage(unitGrodusXImage); return retIV;}
        if(name.equals("Magnus Von Grapple")){retIV.setImage(unitMagnusVonGrappleImage); return retIV;}
        if(name.equals("X-Fist")){retIV.setImage(unitMagnusVonGrappleImage); return retIV;}
        if(name.equals("Magnus Von Grapple 2.0")){retIV.setImage(unitMagnusVonGrapple2Image); return retIV;}
        if(name.equals("X-Punch")){retIV.setImage(unitMagnusVonGrapple2Image); return retIV;}
        if(name.equals("Lord Crump")){retIV.setImage(unitLordCrumpImage); return retIV;}
        if(name.equals("Grodus")){retIV.setImage(unitGrodusImage); return retIV;}
        if(name.equals("Beldam")){retIV.setImage(unitBeldamImage); return retIV;}
        if(name.equals("Marilyn")){retIV.setImage(unitMarilynImage); return retIV;}
        if(name.equals("Shadow Peach")){retIV.setImage(unitShadowPeachImage); return retIV;}
        if(name.equals("Shadow Queen")){retIV.setImage(unitShadowQueenImage); return retIV;}
        if(name.equals("Shadow Hand 1")){retIV.setImage(unitShadowHand1Image); return retIV;}
        if(name.equals("Shadow Hand 2")){retIV.setImage(unitShadowHand2Image); return retIV;}
        if(name.equals("Small Shadow Hands")){retIV.setImage(unitSmallShadowHandsImage); return retIV;}
        if(name.equals("Mario")){retIV.setImage(marioHeadCustomImage); return retIV;}
        if(name.equals("Goombella")){retIV.setImage(goombellaPartnerSwitchImage); return retIV;}
        if(name.equals("Koops")){retIV.setImage(koopsPartnerSwitchImage); return retIV;}
        if(name.equals("Flurrie")){retIV.setImage(flurriePartnerSwitchImage); return retIV;}
        if(name.equals("Yoshi")){retIV.setImage(yoshiPartnerSwitchImage); return retIV;}
        if(name.equals("Vivian")){retIV.setImage(vivianPartnerSwitchImage); return retIV;}
        if(name.equals("Bobbery")){retIV.setImage(bobberyPartnerSwitchImage); return retIV;}
        if(name.equals("Ms. Mowz")){retIV.setImage(mowzPartnerSwitchImage); return retIV;}
        if(name.equals("Bomb Squad Bomb")){retIV.setImage(unitBombSquadBombImage); return retIV;}
        if(name.equals("Shell Shield")){retIV.setImage(unitShellShieldImage); return retIV;}
        if(name.equals("unit_system")){retIV.setImage(itemsIconImage); return retIV;}
        if(name.equals("unit_npc_christine")){retIV.setImage(goombellaPartnerSwitchImage); return retIV;}
        if(name.equals("unit_lecture_christine")){retIV.setImage(goombellaPartnerSwitchImage); return retIV;}
        if(name.equals("unit_lecture_frankli")){retIV.setImage(unitProfessorFranklyImage); return retIV;}
        if(name.equals("unit_lecture_frankli_sac")){retIV.setImage(unitProfessorFranklyImage); return retIV;}
        if(name.equals("unit_lecture_frankli_kuriboo")){retIV.setImage(unitProfessorFranklyImage); return retIV;}
        if(name.equals("unit_act_atmic_teresa")){retIV.setImage(unitAtomicBooImage); return retIV;}
        if(name.equals("unit_act_clauda")){retIV.setImage(flurriePartnerSwitchImage); return retIV;}
        if(name.equals("unit_act_kinopiko")){retIV.setImage(femaleToadCustomImage); return retIV;}
        if(name.equals("unit_act_kinopio")){retIV.setImage(toadCustomImage); return retIV;}
        if(name.equals("unit_act_mario")){retIV.setImage(unitDooplissMarioImage); return retIV;}
        if(name.equals("unit_act_teresa")){retIV.setImage(unitBooImage); return retIV;}

        return retIV;
    }

    public String fileNameSelector(String name)
    {
        String retString = name;

        if(name.equals("main.dol")){retString = "System"; return retString;}
        if(name.equals("Start.dol")){retString = "System"; return retString;}
        if(name.equals("aji.rel")){retString = "X-Naut Fortress"; return retString;}
        if(name.equals("bom.rel")){retString = "Fahr Outpost"; return retString;}
        if(name.equals("dou.rel")){retString = "Pirate's Grotto"; return retString;}
        if(name.equals("eki.rel")){retString = "Riverside Station"; return retString;}
        if(name.equals("gon.rel")){retString = "Hooktail Castle"; return retString;}
        if(name.equals("gor.rel")){retString = "Rogueport"; return retString;}
        if(name.equals("gra.rel")){retString = "Twilight Trail"; return retString;}
        if(name.equals("hei.rel")){retString = "Petal Meadows"; return retString;}
        if(name.equals("jin.rel")){retString = "Creepy Steeple"; return retString;}
        if(name.equals("jon.rel")){retString = "Pit of 100 Trials"; return retString;}
        if(name.equals("las.rel")){retString = "Palace of Shadow"; return retString;}
        if(name.equals("moo.rel")){retString = "The Moon"; return retString;}
        if(name.equals("mri.rel")){retString = "Boggly Tree"; return retString;}
        if(name.equals("muj.rel")){retString = "Keelhaul Key"; return retString;}
        if(name.equals("nok.rel")){retString = "Petalburg"; return retString;}
        if(name.equals("pik.rel")){retString = "Poshley Heights"; return retString;}
        if(name.equals("rsh.rel")){retString = "Excess Express"; return retString;}
        if(name.equals("tik.rel")){retString = "Rogueport Sewers"; return retString;}
        if(name.equals("tou2.rel")){retString = "Glitz Pit"; return retString;}
        if(name.equals("win.rel")){retString = "Boggly Woods"; return retString;}

        return retString;
    }

    public ImageView fileImageSelector(String name)
    {
        ImageView retIV = new ImageView(unknownImage);
        retIV.setFitHeight(15); retIV.setFitWidth(15);

        if(name.equals("main.dol")){retIV.setImage(magicalMap2Image); return retIV;}
        if(name.equals("Start.dol")){retIV.setImage(magicalMap2Image); return retIV;}
        if(name.equals("aji.rel")){retIV.setImage(unitXNautImage); return retIV;}
        if(name.equals("bom.rel")){retIV.setImage(fahrOutpostBombCustomImage); return retIV;}
        if(name.equals("dou.rel")){retIV.setImage(unitBillBlasterImage); return retIV;}
        if(name.equals("eki.rel")){retIV.setImage(unitRuffPuffImage); return retIV;}
        if(name.equals("gon.rel")){retIV.setImage(unitHooktailImage); return retIV;}
        if(name.equals("gor.rel")){retIV.setImage(unitProfessorFranklyImage); return retIV;}
        if(name.equals("gra.rel")){retIV.setImage(unitHyperGoombaImage); return retIV;}
        if(name.equals("hei.rel")){retIV.setImage(unitKoopaTroopaImage); return retIV;}
        if(name.equals("jin.rel")){retIV.setImage(unitDooplissImage); return retIV;}
        if(name.equals("jon.rel")){retIV.setImage(unitBonetailImage); return retIV;}
        if(name.equals("las.rel")){retIV.setImage(unitShadowPeachImage); return retIV;}
        if(name.equals("moo.rel")){retIV.setImage(unitMoonCleftImage); return retIV;}
        if(name.equals("mri.rel")){retIV.setImage(puniCustomImage); return retIV;}
        if(name.equals("muj.rel")){retIV.setImage(unitPutridPiranhaImage); return retIV;}
        if(name.equals("nok.rel")){retIV.setImage(koopaCustomImage); return retIV;}
        if(name.equals("pik.rel")){retIV.setImage(unitDarkBooImage); return retIV;}
        if(name.equals("rsh.rel")){retIV.setImage(unitSmorgImage); return retIV;}
        if(name.equals("tik.rel")){retIV.setImage(unitBlooperImage); return retIV;}
        if(name.equals("tou2.rel")){retIV.setImage(unitRawkHawkImage); return retIV;}
        if(name.equals("win.rel")){retIV.setImage(unitPalePiranhaImage); return retIV;}

        return retIV;
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}