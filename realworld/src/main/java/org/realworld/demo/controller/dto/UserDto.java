package org.realworld.demo.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.realworld.demo.domain.user.entity.User;

public class UserDto {

  private UserDto() {
  }

  public static class UserUpdateRequest {

    @JsonProperty(value = "user")
    public UserUpdateRequest.Request user;

    public String getEmail() {
      return user.email;
    }

    public String getUsername() {
      return user.username;
    }

    public String getPassword() {
      return user.password;
    }

    public String getImage() {
      return user.image;
    }

    public String getBio() {
      return user.bio;
    }

    public static class Request {

      public final String email;

      public final String username;

      public final String password;

      public final String image;

      public final String bio;

      public Request(String email, String username, String password, String image, String bio) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
        this.bio = bio;
      }

    }
  }

  public static class UserCreateRequest {

    @JsonProperty(value = "user")
    public UserCreateRequest.Request user;

    public User toUser() {
      return user.toUser();
    }

    public static class Request {

      private final String username;

      private final String email;

      private final String password;

      public Request(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
      }

      public User toUser() {
        return new User(email, password, username, "", "");
      }
    }
  }

  public static class UserResponse {

    @JsonProperty(value = "user")
    public Response response;

    public Response getResponse() {
      return response;
    }

    public static class Response {

      private final String email;

      private final String token;

      private final String username;

      private final String bio;

      private final String image;

      private Response(String email, String token, String username, String bio, String image) {
        this.email = email;
        this.token = token;
        this.username = username;
        this.bio = bio;
        this.image = image;
      }

      public String getEmail() {
        return email;
      }

      public String getToken() {
        return token;
      }

      public String getUsername() {
        return username;
      }

      public String getBio() {
        return bio;
      }

      public String getImage() {
        return image;
      }
    }

    public UserResponse(Response response) {
      this.response = response;
    }

    public static UserResponse from(User user, String jwtToken) {
      return new UserResponse(
          new Response(user.getEmail(), jwtToken, user.getUsername(), user.getBio(),
              user.getImage())
      );
    }
  }

  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class UserLoginRequest {

    @JsonProperty(value = "user")
    public Request request;

    public String getEmail() {
      return request.email;
    }

    public String getPassword() {
      return request.password;
    }


    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {

      public String email;

      public String password;

      public Request(String email, String password) {
        this.email = email;
        this.password = password;
      }

    }
  }
}
