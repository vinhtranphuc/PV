package com.so.security.oauth2;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.core.AuthenticationException;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;

/*import com.so.exception.AppException;
import com.so.exception.OAuth2AuthenticationProcessingException;
import com.so.jpa.entity.AuthProvider;
import com.so.jpa.entity.Role;
import com.so.jpa.entity.RoleName;s
import com.so.jpa.entity.User;
import com.so.jpa.repository.UserRepository;
import com.so.security.UserPrincipal;
import com.so.security.oauth2.user.OAuth2UserInfo;
import com.so.security.oauth2.user.OAuth2UserInfoFactory;
import com.so.service.AuthService;

import java.util.Collections;
import java.util.Optional;*/

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private AuthService userService;

//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
//
//        try {
//            return processOAuth2User(oAuth2UserRequest, oAuth2User);
//        } catch (AuthenticationException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
//            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
//        }
//    }

//    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
//        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
//        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
//            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
//        }
//
//        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
//        User user;
//        if(userOptional.isPresent()) {
//            user = userOptional.get();
//            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
//                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
//                        user.getProvider() + " account. Please use your " + user.getProvider() +
//                        " account to login.");
//            }
//            user = updateExistingUser(user, oAuth2UserInfo);
//        } else {
//            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
//        }
//
//        return UserPrincipal.create(user, oAuth2User.getAttributes());
//    }
//
//    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
//        User user = new User();
//
//        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
//        user.setProvider_id(oAuth2UserInfo.getId());
//        user.setFull_name(oAuth2UserInfo.getName());
//        user.setEmail(oAuth2UserInfo.getEmail());
//        user.setSocial_avatar_url(oAuth2UserInfo.getImageUrl());
//        Role userRole = userService.getRoleByName(RoleName.ROLE_USER)
//				.orElseThrow(() -> new AppException("User Role not set."));
//		user.setRoles(Collections.singleton(userRole));
//        return userRepository.save(user);
//    }

//    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
//        existingUser.setFull_name(oAuth2UserInfo.getName());
//        existingUser.setSocial_avatar_url(oAuth2UserInfo.getImageUrl());
//        return userRepository.save(existingUser);
//    }

}
