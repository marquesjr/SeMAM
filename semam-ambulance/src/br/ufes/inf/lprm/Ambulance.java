package br.ufes.inf.lprm;

import java.io.IOException;

class Speed {

}

class Position {



}

public class Ambulance {

    private static String server_url;
    private static String id;
    private static Position location;
    private static Speed average_speep;

    //static Position

    private static void Register() throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(server_url + "/context/ambulance/register");

            System.out.println("executing request " + httpget.getURI());

            // Create a custom response handler
            ResponseHandler<JSONObject> responseHandler = new ResponseHandler<JSONObject>() {

                public JSONObject handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();

                        if (entity != null) {
                            try {
                                JSONObject result = new JSONObject(EntityUtils.toString(entity));
                                return result;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        } else return null;

                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            JSONObject responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");

        } finally {
            httpclient.close();
        }

    }

    private static void LookForAssignment() {




    }

    public static void Main(String[] args) {

        server_url = args[1];

        try {
            Register();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //register on semam-server

    }

}
