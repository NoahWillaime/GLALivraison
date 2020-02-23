/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infos;

import jms.Livraison;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author noahd
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/LivraisonQueue")
})
public class LivraisonMDB implements MessageListener {
    @EJB(name="AccuserReceptionBean")
    private AccuserReceptionBeanLocal accuserRecep;
    
    @Resource
    private MessageDrivenContext context;
    
    @Override
    public void onMessage(Message message) {
        try {
            message.acknowledge();
            Object obj = message.getBody(Object.class);
            processFacturation(obj);
        } catch (JMSException jmse) {
            jmse.printStackTrace();
            context.setRollbackOnly();
        }
    }
    
    public void processFacturation(Object obj) {
        accuserRecep.sendOrder(obj);
    }
}
