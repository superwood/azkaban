/*
 * Copyright 2012 LinkedIn Corp.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package azkaban.execapp.event;

import azkaban.execapp.FlowRunner;
import azkaban.execapp.JobRunner;
import azkaban.execapp.event.Event.Type;
import azkaban.executor.ExecutableNode;

public class LocalFlowWatcher extends FlowWatcher {
	private LocalFlowWatcherListener watcherListener;
	private FlowRunner runner;
	private boolean isShutdown = false;
	
	public LocalFlowWatcher(FlowRunner runner) {
		super(runner.getExecutableFlow().getExecutionId());
		super.setFlow(runner.getExecutableFlow());
		
		watcherListener = new LocalFlowWatcherListener();
		this.runner = runner;
		runner.addListener(watcherListener);
	}

	@Override
	public void stopWatcher() {
		// Just freeing stuff
		if(isShutdown) {
			return;
		}
		
		isShutdown = true;
		runner.removeListener(watcherListener);
		try {
			getLogger().info("Stopping watcher, and unblocking pipeline. pepelineexecId:" + getExecId() + " currentexecId: " + runner.getExecId()+" watch:"+this);
		}catch (Exception e){
			e.printStackTrace();
		}
		runner = null;
		super.failAllWatches();
	}

	public class LocalFlowWatcherListener implements EventListener {
		@Override
		public void handleEvent(Event event) {
			if (event.getType() == Type.JOB_FINISHED) {
				if (event.getRunner() instanceof FlowRunner) {
					Object data = event.getData();
					if (data instanceof ExecutableNode) {
						ExecutableNode node = (ExecutableNode)data;
						handleJobFinished(node.getJobId(), node.getStatus());
					}
				}
				else if (event.getRunner() instanceof JobRunner) {
					JobRunner runner = (JobRunner)event.getRunner();
					ExecutableNode node = runner.getNode();
					
					handleJobFinished(node.getJobId(), node.getStatus());
				}
			}
			else if (event.getType() == Type.FLOW_FINISHED) {
				try {
					getLogger().info("Stopping watcher,  pepelineexecId:" + getExecId() + " currentexecId: " + runner.getExecId());
				}catch (Exception e){
					e.printStackTrace();
				}
				stopWatcher();
			}
		}
	}
}
