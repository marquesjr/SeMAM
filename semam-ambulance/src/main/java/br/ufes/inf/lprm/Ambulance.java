package br.ufes.inf.lprm;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

class Speed {
    private double average;

    double getAverage() {
        return average;
    }

    void setAverage(double average) {
        this.average = average;
    }

}

class Position {
    private double longitude;
    private double latitude;

    public Position() {

    }

    public Position(double lt, double ln) {
        latitude = lt;
        longitude = ln;
    }

    double getLatitude() {
        return latitude;
    }

    void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object e) {
        if (e == null) return false;
        if (e instanceof Position) {
           return (((Position) e).getLatitude() == this.latitude) && (((Position) e).getLongitude() == this.longitude);
        } else return false;
    }

    @Override
    public String toString() {
        return this.getLatitude() + "," + this.getLongitude();
    }
}

public class Ambulance {

    private static String server_url;
    private static String id;
    private static Position location;
    private static Speed average_speep;

    //static Position

    private static JSONObject sendGetRequest(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<JSONObject> responseHandler = new ResponseHandler<JSONObject>() {
                public JSONObject handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    if (status >= 200 && status < 300) {
                        if (entity != null) {
                            try {
                                String msg = EntityUtils.toString(entity);
                                if (!msg.isEmpty()) {
                                    JSONObject result = new JSONObject(msg);
                                    return result;
                                } else return null;
                            } catch (JSONException e) {
                                e.printStackTrace();
                                return null;
                            }
                        } else return null;

                    } else {
                        System.out.println(EntityUtils.toString(entity));
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httpget, responseHandler);
        } finally {
            httpclient.close();
        }
    }

    private static JSONObject sendPostRequest(String url, JSONObject message) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-type", "application/json");

            if (message != null) {
                httppost.setEntity(new StringEntity(message.toString()));
            }
            ResponseHandler<JSONObject> responseHandler = new ResponseHandler<JSONObject>() {
                public JSONObject handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    if (status >= 200 && status < 300) {
                        return null;
                    } else {
                        System.out.println(EntityUtils.toString(entity));
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            return httpclient.execute(httppost, responseHandler);
        } finally {
            httpclient.close();
        }
    }


    private static void register() throws IOException {

        if (id == null) {
            JSONObject response = sendGetRequest(server_url + "/context/ambulance/register");
            if (response != null) {
                try {
                    id = response.getString("id");
                    average_speep = new Speed();
                    average_speep.setAverage(response.getJSONObject("speed").getDouble("average"));
                    location = new Position(response.getJSONObject("location").getDouble("latitude"),
                                            response.getJSONObject("location").getDouble("longitude"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("----------------------------------------");
                System.out.println("ambulance successfully registered with id (" + id + ")");
                System.out.println("----------------------------------------");
            } else {
                System.out.println("----------------------------------------");
                System.out.println("BAD REQUEST");
                System.out.println("----------------------------------------");
            }
        }
    }

    private static String getMapsURL(Position origin, Position destination) {
        return "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin.toString() + "&destination=" + destination.toString() + "&sensor=false";
    }

    private static void toRescue(Position destination) throws IOException {

        Position origin = location;

        JSONObject response = sendGetRequest(getMapsURL(origin, destination));

        if (response != null) {
            try {

                System.out.println("----------------------------------------");
                System.out.println(response);
                System.out.println("----------------------------------------");

                JSONObject route = response.getJSONArray("routes").getJSONObject(0);
                int total_dist   = route.getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");
                String polyline  = route.getJSONObject("overview_polyline").getString("points");

                System.out.println("PATH: " + polyline + "\n" + "DISTANCE: " + total_dist);


                //go for patient
                Move(polyline, total_dist);

                location = destination;

                JSONObject message = new JSONObject();
                JSONObject l = new JSONObject();
                l.put("latitude", location.getLatitude());
                l.put("longitude", location.getLongitude());
                message.put("location", l);
                sendPostRequest(server_url + "/context/ambulance/" + id + "/arrived", message);

                //ask new position
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //esperar atendimento

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //response = null;
        response = sendGetRequest(getMapsURL(destination, origin));

        if (response != null) {

            try {

                System.out.println("----------------------------------------");
                System.out.println(response);
                System.out.println("----------------------------------------");

                JSONObject route = response.getJSONArray("routes").getJSONObject(0);
                int total_dist   = route.getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");
                String polyline  = route.getJSONObject("overview_polyline").getString("points");

                System.out.println("PATH: " + polyline + "\n" + "DISTANCE: " + total_dist);

                Move(polyline, total_dist);

                location = origin;

                JSONObject message = new JSONObject();
                JSONObject l = new JSONObject();
                l.put("latitude", location.getLatitude());
                l.put("longitude", location.getLongitude());
                message.put("location", l);

                sendPostRequest(server_url + "/context/ambulance/" + id + "/solved", message);

                //ask new position
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private static void Move(String poly, int distance) {
        int current_distance = 0;
        int step = 100;

        while (current_distance < distance) {
            current_distance+=step;
            if (current_distance > distance) {
                current_distance = distance;
            }
            try {
                JSONObject message = new JSONObject();
                message.put("polyline", poly);
                message.put("distance", current_distance);
                System.out.print("Current distance: " + current_distance);
                sendPostRequest(server_url + "/context/ambulance/" + id + "/move", message);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("PATH COMPLETED");
    }

    private static void lookForAssignment() throws IOException {
        if (id != null) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("looking for assignment...");

            JSONObject response = sendGetRequest(server_url + "/context/ambulance/" + id + "/assignment");
            if (response != null) {
                try {
                    toRescue(new Position(response.getJSONObject("location").getDouble("latitude"),
                            response.getJSONObject("location").getDouble("longitude")));
                } catch (JSONException e) {
                  e.printStackTrace();
                }

                System.out.println("----------------------------------------");
                System.out.println(response);
                System.out.println("----------------------------------------");
            } else {
                System.out.println("no assignment...");
            }
            lookForAssignment();
        }
    }

    public static void main(String [] args) {

        if (args.length > 0) {
            server_url = args[0];
        } else {
            server_url = "http://localhost:9000";
        }

        try {
            register();
            lookForAssignment();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            //Unregister();
        }


        //register on semam-server

    }

}
