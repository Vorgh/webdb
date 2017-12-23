export class ConnectionAuthInfo
{
  url: string;
  username: string;
  password: string;
}

export class OAuthTokenResponse
{
  access_token: string;
  token_type: string;
  expires_in: number;
  scope: string;
}
