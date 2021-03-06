package company.sdk.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import company.sdk.connection.ResponseCallback;
import company.sdk.connection.ResponseParser;
import company.sdk.connection.ServerConnection;
import company.sdk.encryption.Crypter;
import company.sdk.model.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by aleksanderkristiansen on 20/11/2016.
 */
public class BookService {

    private ServerConnection connection;
    private Gson gson;

    public BookService(){
        this.connection = new ServerConnection();
        this.gson = new Gson();
    }

    public void createBook(Book book, final ResponseCallback<String> responseCallback){

        HttpPost postRequest = new HttpPost(ServerConnection.serverURL + "/book");


        String bookS = this.gson.toJson(book);

        String bookC = Crypter.encryptDecryptXOR(bookS);

        try{

            StringEntity bookString = new StringEntity(bookC);

            postRequest.setEntity(bookString);
            postRequest.setHeader("Content-Type", "application/json");

            this.connection.execute(postRequest, new ResponseParser() {
                public void payload(String json) {
                    responseCallback.success(json);
                }

                public void error(int status) {
                    responseCallback.error(status);
                }
            });

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public void getBook(int id, final ResponseCallback<Book> responseCallback){
        HttpGet getRequest = new HttpGet(ServerConnection.serverURL + "/book/" + id);

        this.connection.execute(getRequest, new ResponseParser() {
            public void payload(String json) {
                String bookC = Crypter.encryptDecryptXOR(json);
                Book book = gson.fromJson(bookC, Book.class);
                responseCallback.success(book);
            }

            public void error(int status) {
                responseCallback.error(status);
            }
        });
    }


    public void getAuthors(final ResponseCallback<ArrayList<Author>> responseCallback){
        HttpGet getRequest = new HttpGet(ServerConnection.serverURL + "/book/authors");

        this.connection.execute(getRequest, new ResponseParser() {
            public void payload(String json) {
                ArrayList<Author> authors = gson.fromJson(Crypter.encryptDecryptXOR(json), new TypeToken<ArrayList<Author>>() {
                }.getType());
                responseCallback.success(authors);
            }

            public void error(int status) {
                responseCallback.error(status);
            }
        });
    }

    public void getBookstores(final ResponseCallback<ArrayList<BookStore>> responseCallback){
        HttpGet getRequest = new HttpGet(ServerConnection.serverURL + "/book/bookstores");

        this.connection.execute(getRequest, new ResponseParser() {
            public void payload(String json) {
                ArrayList<BookStore> bookStores = gson.fromJson(Crypter.encryptDecryptXOR(json), new TypeToken<ArrayList<BookStore>>() {
                }.getType());
                responseCallback.success(bookStores);
            }

            public void error(int status) {
                responseCallback.error(status);
            }
        });
    }

    public void getPublishers(final ResponseCallback<ArrayList<Publisher>> responseCallback){
        HttpGet getRequest = new HttpGet(ServerConnection.serverURL + "/book/publishers");

        this.connection.execute(getRequest, new ResponseParser() {
            public void payload(String json) {
                ArrayList<Publisher> publisher = gson.fromJson(Crypter.encryptDecryptXOR(json), new TypeToken<ArrayList<Publisher>>() {
                }.getType());
                responseCallback.success(publisher);
            }

            public void error(int status) {
                responseCallback.error(status);
            }
        });
    }


}
