/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.webservice;

/**
 * WebServiceMethod Interface for the Strategy Pattern
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
interface IWebServiceMethod {

    /**
     * @return Elgg HTTP status code
     * 0 = OK
     * -1 = Error
     * -20 = Token not valid
     * -30 = No Network Connection
     */
    Long execute();


}
