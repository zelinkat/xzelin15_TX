
package cz.vutbr.fit.xzelin15.dp.restaurant;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.2-hudson-182-RC1
 * Generated source version: 2.0
 * 
 */
@WebService(name = "IRestaurantServiceBA", targetNamespace = "http://www.jboss.com/jbosstm/xts/demo/Restaurant")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IRestaurantServiceBA {


    /**
     * 
     * @param howMany
     * @return
     *     returns boolean
     */
    @WebMethod
    @WebResult(name = "bookSeatsBAResponse", partName = "bookSeatsBAResponse")
    public boolean bookSeats(
        @WebParam(name = "how_many", partName = "how_many")
        int howMany);

}
