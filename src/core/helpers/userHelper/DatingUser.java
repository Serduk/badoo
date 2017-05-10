package core.helpers.userHelper;

import exceptions.BadooException;

import java.util.Objects;

/**
 * Helper class for quick users registration
 * create new Object User when initiated;
 * <p>
 * Created by Serdyuk on 13.06.2017.
 */
public class DatingUser extends AUser {

    private boolean camCredits = false;

    public DatingUser(String site, String location, String source, String platform) {

        super(site, location, source, platform);
        boolean isMobSiteRegistration = Objects.equals(this.platform, "mobSite");

        outputData = helper.setData(inputData)
                .datingRegistration(isMobSiteRegistration)
                .findDatingUser()
                .getData();


        userId = outputData.get("userId");
        autologin = outputData.get("autologinLink");
        autologinKey = outputData.get("autologinKey");

    }


    public DatingUser buy(PaidStatus BuyPackage) {

        switch (BuyPackage.toString()) {
            case "membership":
                helper.buyMembership()
                        .oneMonth();
                break;
            case "features":
                helper.buyMembership()
                        .oneMonth()
                        .buyFeatures()
                        .fullUpgrade();
                break;
            case "camsCredits":
                helper.buyMembership()
                        .oneMonth()
                        .buyFeatures()
                        .fullUpgrade()
                        .buyLiveCamCredits()
                        .buy75();
                camCredits = true;
                break;
        }
        return this;

    }

    public DatingUser coregistration() {

        if (!camCredits) {
            try {
                throw new BadooException("You need to buy camsCredits first! (DatingUser.buy(camCredits))");
            }
            catch (BadooException e) {
                e.printStackTrace();
            }
        }
        outputData = helper.findCamsUser()
                .getData();
        userId = outputData.get("userId");
        autologin = outputData.get("autologinLink");
        autologinKey = outputData.get("autologinKey");

        return this;

    }
}

