package com.example.tickoff;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

public class RequestTask extends AsyncTask<Void, Void, Response> {
    private String requestUrl;
    private String requestType;
    private String requestParams;
    private Context context;
    private OutResponse outResponse;

    public RequestTask(Context context, String requestUrl, String requestType) {
        this.requestUrl = requestUrl;
        this.requestType = requestType;
        this.context = context;
        this.outResponse = (OutResponse) context;
    }

    public RequestTask(Context context, String requestUrl, String requestType, String requestParams) {
        this.requestUrl = requestUrl;
        this.requestType = requestType;
        this.requestParams = requestParams;
        this.context = context;
        this.outResponse = (OutResponse) context;
    }

    @Override
    protected Response doInBackground(Void... voids) {
        Response response = null;
        try {
            switch (requestType){
                case "GET":
                    response = RequestHandler.get(requestUrl);
                    break;
                case "POST":
                    response = RequestHandler.post(requestUrl, requestParams);
                    break;
                case "PUT":
                    response = RequestHandler.put(requestUrl, requestParams);
                    break;
                case "DELETE":
                    response = RequestHandler.delete(requestUrl);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        outResponse.response(response);
    }
    public interface OutResponse {
        void response(Response response);
    }
}
