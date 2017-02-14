package com.mesosphere.dcos.kafka.web;

import com.googlecode.protobuf.format.JsonFormat;
import org.apache.mesos.Protos;
import org.apache.mesos.scheduler.TaskKiller;
import org.apache.mesos.state.StateStore;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Updates StateStore
 */
@Path("/v1/volume")
public class VolumeResource {

    private static final Logger logger = LoggerFactory.getLogger(VolumeResource.class);

    private final StateStore stateStore;
    private final TaskKiller taskKiller;
    private final String frameworkName;

    public VolumeResource(StateStore stateStore, TaskKiller taskKiller, String frameworkName) {
        this.stateStore = stateStore;
        this.taskKiller = taskKiller;
        this.frameworkName = frameworkName;
    }

    @Path("/rename/{taskName}")
    @POST
    public Response restartTask(
            @PathParam("taskName") String taskName,
            @QueryParam("newName") String volName) {
        Protos.TaskInfo taskInfo=new Protos.TaskInfo();
        try {
            logger.info("Attempting to fetch TaskInfo for task '{}'", taskName);
            Optional<Protos.TaskInfo> taskInfoOptional = stateStore.fetchTask(taskName);
            if (!taskInfoOptional.isPresent()) {
                return Response.serverError().build();
            }
            taskInfo=taskInfoOptional.get();
        } catch (Exception ex) {
            logger.warn(String.format(
                    "Failed to fetch requested TaskInfo for task '%s'", taskName), ex);
        }
        List<Protos.Resource> resourceList = new ArrayList();
        for ( Protos.Resource resource: taskInfo.getResourcesList()) {
            if (resource.hasReservation() && resource.getName() == "disk"){

            }
            else{
                resourceList.add(resource);
            }
        }Protos.Resource> resourceList =;

        Protos.TaskInfo.newBuilder(taskInfo).;
        stateStore.clearTask(taskName);
        stateStore.storeTasks();
        return Response.serverError().build();
        boolean taskExists = taskKiller.killTask(name, false);

        if (taskExists) {
            return Response.accepted().build();
        } else {
            logger.error("User requested to kill non-existent task: " + name);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
