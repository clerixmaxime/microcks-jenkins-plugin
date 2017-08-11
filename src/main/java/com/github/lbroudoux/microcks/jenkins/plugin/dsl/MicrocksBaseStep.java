/*
 * Licensed to Laurent Broudoux (the "Author") under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. Author licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.github.lbroudoux.microcks.jenkins.plugin.dsl;

import com.github.lbroudoux.microcks.jenkins.plugin.model.IMicrocksPlugin;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Run;
import hudson.model.TaskListener;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.plugins.workflow.steps.AbstractStepImpl;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author laurent
 */
public abstract class MicrocksBaseStep extends AbstractStepImpl implements SimpleBuildStep, Serializable, IMicrocksPlugin {

   protected String apiURL;
   protected String verbose;

   protected MicrocksBaseStep() {
   }

   public String getApiURL() {
      return apiURL;
   }

   @DataBoundSetter
   public void setApiURL(String apiURL) {
      this.apiURL = apiURL != null ? apiURL.trim() : null;
   }

   public String getVerbose() {
      return verbose;
   }

   @DataBoundSetter
   public void setVerbose(String verbose) {
      this.verbose = verbose != null ? verbose.trim() : null;
   }

   // this is the workflow plugin path
   @Override
   public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher,
                       TaskListener listener) throws InterruptedException, IOException {
      this.doIt(run, workspace, launcher, listener);
   }

   // this is the classic jenkins build step path
   @Override
   public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
      return this.doIt(build, launcher, listener);
   }
}
