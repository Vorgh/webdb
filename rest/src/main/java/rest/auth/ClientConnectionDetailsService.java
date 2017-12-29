package rest.auth;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import java.util.*;

public class ClientConnectionDetailsService implements ClientDetailsService
{
    private Map<String, ClientDetails> clientDetailsStore = new HashMap<String, ClientDetails>();

    @Override
    public ClientDetails loadClientByClientId(String id) throws ClientRegistrationException
    {
        ClientDetails details = clientDetailsStore.get(id);

        if (details == null)
        {
            throw new NoSuchClientException("No client with requested id: " + id);
        }

        return details;
    }

    public void addClient(ClientDetails clientDetails)
    {
        clientDetailsStore.put(clientDetails.getClientId(), clientDetails);
    }

    public ClientDetails getClient(String name)
    {
        return clientDetailsStore.get(name);
    }
}
