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

        /*BaseClientDetails details = new BaseClientDetails();
        details.setClientId(id);
        details.setAuthorizedGrantTypes(Arrays.asList("client_credentials", "password") );
        details.setScope(Arrays.asList("read", "write", "trust"));
        details.setResourceIds(Arrays.asList("oauth2-resource"));
        details.setAccessTokenValiditySeconds(3600);
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        details.setAuthorities(authorities);*/

        return details;
    }

    public void setClientDetailsStore(Map<String, ? extends ClientDetails> clientDetailsStore) {
        this.clientDetailsStore = new HashMap<String, ClientDetails>(clientDetailsStore);
    }

    public void addClient(ClientDetails clientDetails)
    {
        clientDetailsStore.put(clientDetails.getClientId(), clientDetails);
    }
}
