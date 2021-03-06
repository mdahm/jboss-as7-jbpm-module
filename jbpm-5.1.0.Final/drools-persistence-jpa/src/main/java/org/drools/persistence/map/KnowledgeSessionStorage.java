package org.drools.persistence.map;

import org.drools.persistence.info.SessionInfo;
import org.drools.persistence.info.WorkItemInfo;

public interface KnowledgeSessionStorage {

    SessionInfo findSessionInfo(Integer sessionId);

    void saveOrUpdate(SessionInfo storedObject);

    void saveOrUpdate(WorkItemInfo workItemInfo);

    Long getNextWorkItemId();

    WorkItemInfo findWorkItemInfo(Long id);

    void remove(WorkItemInfo workItemInfo);

    Integer getNextStatefulKnowledgeSessionId();

}
