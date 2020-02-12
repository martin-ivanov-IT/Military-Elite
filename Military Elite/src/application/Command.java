package application;

import common.MissionImpl;
import common.RepairImpl;
import enumerations.Corp;
import enumerations.State;
import interfaces.Mission;
import interfaces.Soldier;
import models.*;
import interfaces.Private;


import java.util.List;

public class Command {
    private final Army ARMY = new Army();

    public Army getARMY() {
        return ARMY;
    }

    public void execute(String command, List<String> tokens) {

        int id = Integer.parseInt(tokens.get(0));
        String firstName = tokens.get(1);
        String lastName = tokens.get(2);

        Soldier soldier = null;
        if (command.equals("Private")) {
            soldier = new PrivateImpl(id, firstName, lastName,
                    Double.parseDouble(tokens.get(3)));
        } else if (command.equals("LieutenantGeneral")) {
            soldier = new LieutenantGeneralImpl(id, firstName, lastName,
                    Double.parseDouble(tokens.get(3)));


            int[] ids = tokens.stream().skip(4)
                    .mapToInt(Integer::parseInt)
                    .toArray();

            for (int currentId : ids) {
                for (int i = 0; i < this.getARMY().getSoldiers().size(); i++) {
                    if (this.getARMY().getSoldiers().get(i) instanceof PrivateImpl) {
                        if (this.getARMY().getSoldiers().get(i).getId() == currentId) {
                            ((LieutenantGeneralImpl) soldier).addPrivate((Private) this.getARMY().getSoldiers()
                                    .get(i));

                        }
                    }
                }
            }
        } else if (command.equals("Engineer")) {
            String corps = tokens.get(4);

            if (Corp.Airforces.toString().equals(corps) || Corp.Marines.toString().equals(corps)) {
                soldier = new EngineerImpl(id, firstName, lastName,
                        Double.parseDouble(tokens.get(3)),
                        Corp.valueOf(corps)
                );

                String[] repairData = tokens.stream().skip(5).toArray(String[]::new);

                for (int i = 0; i < repairData.length; i += 2) {
                    String name = repairData[i];
                    int workHours = Integer.parseInt(repairData[i + 1]);
                    RepairImpl repair = new RepairImpl(name, workHours);
                    ((EngineerImpl) soldier).addRepair(repair);


                }
            }

        } else if (command.equals("Commando")) {
            String corps = tokens.get(4);

            if (Corp.Airforces.toString().equals(corps) || Corp.Marines.toString().equals(corps)) {
                soldier = new CommandoImpl(id, firstName, lastName,
                        Double.parseDouble(tokens.get(3)),
                        Corp.valueOf(corps)
                );

                String[] missionDate = tokens.stream().skip(5).toArray(String[]::new);

                for (int i = 0; i < missionDate.length; i += 2) {
                    String name = missionDate[i];
                    String state = missionDate[i + 1];

                    if (State.Finished.toString().equals(state) || State.inProgress.toString().equals(state)) {
                        Mission mission = new MissionImpl(name, State.valueOf(state));
                        ((CommandoImpl) soldier).addMission(mission);
                    }


                }
            }
        } else if (command.equals("Spy")) {
            soldier = new SpyImpl(id, firstName, lastName, tokens.get(3));
        }

        if (soldier != null) {
            this.getARMY().assSoldier(soldier);
        }

    }
}
