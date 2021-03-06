/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.servicecomb.company.auth.endpoint.rest;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import io.servicecomb.company.auth.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class AuthenticationController {

  static final String USERNAME = "username";
  static final String PASSWORD = "password";
  static final String TOKEN = "token";

  private final AuthenticationService authenticationService;
  private final AuthorizationHeaderGenerator authorizationHeaderGenerator;

  @Autowired
  AuthenticationController(
      AuthenticationService authenticationService,
      AuthorizationHeaderGenerator authorizationHeaderGenerator) {
    this.authenticationService = authenticationService;
    this.authorizationHeaderGenerator = authorizationHeaderGenerator;
  }

  @RequestMapping(value = "/login", method = POST)
  ResponseEntity<String> login(
      @RequestParam(USERNAME) String username,
      @RequestParam(PASSWORD) String password) {

    String token = authenticationService.authenticate(username, password);
    HttpHeaders headers = authorizationHeaderGenerator.generate(token);

    return new ResponseEntity<>("Welcome, " + username, headers, OK);
  }

  @RequestMapping(value = "/validate", method = POST)
  @ResponseBody
  String validate(@RequestParam(TOKEN) String token) {

    return authenticationService.validate(token);
  }
}
