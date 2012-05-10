package cz.vutbr.fit.xzelin15.dp.recovery;


import static cz.vutbr.fit.xzelin15.dp.state.ServiceStateConstants.*;
import cz.vutbr.fit.xzelin15.dp.taxi.TaxiParticipantAT;
import org.jboss.jbossts.xts.recovery.participant.at.XTSATRecoveryModule;
import org.jboss.jbossts.xts.recovery.participant.at.XTSATRecoveryManager;
import com.arjuna.wst.Durable2PCParticipant;

import java.io.ObjectInputStream;

/**
 * Application-specific WS-AT participant recovery manager for demo application, This class
 * is responsible for recreating application-specific durable participants from records
 * logged at prepare time.
 */
public class DemoATRecoveryModule implements XTSATRecoveryModule
{
    /**
     * the singleton recovery module
     */
    private static DemoATRecoveryModule theRecoveryModule = null;

    /**
     * a count of how many xts demo services are currently installed
     */
    private static int serviceCount = 0;

    /**
     * called during deployment of an xts-demo web service to ensure the recovery module for the
     * demo is installed whenever any of the services is active
     */
    public static void register()
    {
        if (theRecoveryModule == null) {
            theRecoveryModule = new DemoATRecoveryModule();
        }
        if (serviceCount == 0) {
            XTSATRecoveryManager.getRecoveryManager().registerRecoveryModule(theRecoveryModule);
        }
        serviceCount++;
    }

    /**
     * called during undeployment of an xts-demo web service to ensure the recovery module for
     * the demo is deinstalled once none of the services is active
     */
    public static void unregister()
    {
        if (serviceCount > 0) {
            serviceCount--;
            if (serviceCount == 0) {
                XTSATRecoveryManager.getRecoveryManager().unregisterRecoveryModule(theRecoveryModule);
            }
        }
    }
    
    /**
     * called during recovery processing to allow an application to identify a participant id
     * belonging to one of its participants and recreate the participant by deserializing
     * it from the supplied object input stream. n.b. this is only appropriate in case the
     * participant was originally saved using serialization.
     *
     * @param id     the id used when the participant was created
     * @param stream a stream from which the application should deserialise the participant
     *               if it recognises that the id belongs to the module's application
     * @return a recovered durable participant if the id belongs to this application otherwise null
     * @throws Exception if an error occurs deserializing the durable participant
     */
    public Durable2PCParticipant deserialize(String id, ObjectInputStream stream) throws Exception
    {
        if (id.startsWith("org.jboss.jbossts.xts-demo:taxiAT")) {
            System.out.println("xts-demo : attempting to deserialize TaxiParticipantAT " + id);
            TaxiParticipantAT participant = (TaxiParticipantAT)stream.readObject();
            System.out.println("xts-demo : deserialized TaxiParticipantAT " + id);
            return participant;
        }

        return null;
    }

    /**
     * called during recovery processing to allow an application to identify a participant id
     * belonging to one of its participants and use the saved recovery state to recreate the
     * participant. n.b. this is only appropriate in case the participant was originally saved
     * after being converted to a byte array using the PersistibleATParticipant interface.
     *
     * @param id            the id used when the participant was created
     * @param recoveryState a byte array returned form the original participant via a call to
     *                      method getRecoveryState of interface PersistableATParticipant
     * @return null as the demo application does not use this recovery method
     * @throws Exception if the id belongs to this application
     */
    public Durable2PCParticipant recreate(String id, byte[] recoveryState) throws Exception {
        if (id.startsWith("org.jboss.jbossts.xts-demo:restauarantAT") ||
                id.startsWith("org.jboss.jbossts.xts-demo:theatreAT") ||
                id.startsWith("org.jboss.jbossts.xts-demo:taxiAT")) {
            // this should not get called -- xts-demo WS-AT participants are saved and restored
            // using serialization
            throw new Exception("xts-demo : invalid request to recreate() WS-AT participant " + id);
        }
        return null;
    }

    /**
     * flags used to identify the first endScan call.
     */
    private boolean isFirst = true;

    /**
     * participant recovery modules may need to perform special processing when the a recovery scan has
     * completed. in particular it is only after the first recovery scan has completed they can identify
     * whether locally prepared changes are accompanied by a recreated participant and roll back changes
     * for those with no such participant.
     */
    public void endScan()
    {
        if (isFirst) {
            isFirst = false;
        }
    }
}
