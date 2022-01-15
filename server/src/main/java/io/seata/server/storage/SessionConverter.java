/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.server.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import io.seata.common.util.CollectionUtils;
import io.seata.common.util.StringUtils;
import io.seata.core.console.vo.BranchSessionVO;
import io.seata.core.console.vo.GlobalSessionVO;
import io.seata.core.model.BranchStatus;
import io.seata.core.model.BranchType;
import io.seata.core.model.GlobalStatus;
import io.seata.core.store.BranchTransactionDO;
import io.seata.core.store.GlobalTransactionDO;
import io.seata.server.session.BranchSession;
import io.seata.server.session.GlobalSession;
import io.seata.server.store.SessionStorable;
import org.springframework.beans.BeanUtils;

/**
 * The session converter
 *
 * @author wangzhongxiang
 * @author doubleDimple
 */
public class SessionConverter {

    public static GlobalSession convertGlobalSession(GlobalTransactionDO globalTransactionDO) {
        if (globalTransactionDO == null) {
            return null;
        }
        GlobalSession session = new GlobalSession(globalTransactionDO.getApplicationId(),
                globalTransactionDO.getTransactionServiceGroup(),
                globalTransactionDO.getTransactionName(),
                globalTransactionDO.getTimeout());
        session.setXid(globalTransactionDO.getXid());
        session.setTransactionId(globalTransactionDO.getTransactionId());
        session.setStatus(GlobalStatus.get(globalTransactionDO.getStatus()));
        session.setApplicationData(globalTransactionDO.getApplicationData());
        session.setBeginTime(globalTransactionDO.getBeginTime());
        return session;
    }

    public static BranchSession convertBranchSession(BranchTransactionDO branchTransactionDO) {
        if (branchTransactionDO == null) {
            return null;
        }
        BranchSession branchSession = new BranchSession();
        branchSession.setXid(branchTransactionDO.getXid());
        branchSession.setTransactionId(branchTransactionDO.getTransactionId());
        branchSession.setApplicationData(branchTransactionDO.getApplicationData());
        branchSession.setBranchId(branchTransactionDO.getBranchId());
        branchSession.setBranchType(BranchType.valueOf(branchTransactionDO.getBranchType()));
        branchSession.setResourceId(branchTransactionDO.getResourceId());
        branchSession.setClientId(branchTransactionDO.getClientId());
        branchSession.setResourceGroupId(branchTransactionDO.getResourceGroupId());
        branchSession.setStatus(BranchStatus.get(branchTransactionDO.getStatus()));
        return branchSession;
    }

    public static GlobalTransactionDO convertGlobalTransactionDO(SessionStorable session) {
        if (session == null || !(session instanceof GlobalSession)) {
            throw new IllegalArgumentException(
                    "The parameter of SessionStorable is not available, SessionStorable:" + StringUtils.toString(session));
        }
        GlobalSession globalSession = (GlobalSession)session;

        GlobalTransactionDO globalTransactionDO = new GlobalTransactionDO();
        globalTransactionDO.setXid(globalSession.getXid());
        globalTransactionDO.setStatus(globalSession.getStatus().getCode());
        globalTransactionDO.setApplicationId(globalSession.getApplicationId());
        globalTransactionDO.setBeginTime(globalSession.getBeginTime());
        globalTransactionDO.setTimeout(globalSession.getTimeout());
        globalTransactionDO.setTransactionId(globalSession.getTransactionId());
        globalTransactionDO.setTransactionName(globalSession.getTransactionName());
        globalTransactionDO.setTransactionServiceGroup(globalSession.getTransactionServiceGroup());
        globalTransactionDO.setApplicationData(globalSession.getApplicationData());
        return globalTransactionDO;
    }

    public static BranchTransactionDO convertBranchTransactionDO(SessionStorable session) {
        if (session == null || !(session instanceof BranchSession)) {
            throw new IllegalArgumentException(
                    "The parameter of SessionStorable is not available, SessionStorable:" + StringUtils.toString(session));
        }
        BranchSession branchSession = (BranchSession)session;
        BranchTransactionDO branchTransactionDO = new BranchTransactionDO();
        branchTransactionDO.setXid(branchSession.getXid());
        branchTransactionDO.setBranchId(branchSession.getBranchId());
        branchTransactionDO.setBranchType(branchSession.getBranchType().name());
        branchTransactionDO.setClientId(branchSession.getClientId());
        branchTransactionDO.setResourceGroupId(branchSession.getResourceGroupId());
        branchTransactionDO.setTransactionId(branchSession.getTransactionId());
        branchTransactionDO.setApplicationData(branchSession.getApplicationData());
        branchTransactionDO.setResourceId(branchSession.getResourceId());
        branchTransactionDO.setStatus(branchSession.getStatus().getCode());
        return branchTransactionDO;
    }

    public static void convertToGlobalSessionVo(List<GlobalSessionVO> result, List<GlobalSession> globalSessions) {
        if (CollectionUtils.isNotEmpty(globalSessions)){
            for (GlobalSession globalSession : globalSessions) {
                GlobalSessionVO globalSessionVO = new GlobalSessionVO();
                BeanUtils.copyProperties(globalSession,globalSessionVO);
                globalSessionVO.setStatus(0);
                globalSessionVO.setTimeout(Long.valueOf(globalSession.getTimeout()));
                globalSessionVO.setBranchSessionVOs(converToBranchSession(globalSession.getBranchSessions()));
                result.add(globalSessionVO);
            }
        }
    }

    public static Set<BranchSessionVO> converToBranchSession(ArrayList<BranchSession> branchSessions) {
        Set<BranchSessionVO> branchSessionVOs = new HashSet<>(branchSessions.size());
        if (CollectionUtils.isNotEmpty(branchSessions)){
            for (BranchSession branchSession : branchSessions) {
                BranchSessionVO branchSessionVONew = new BranchSessionVO();
                BeanUtils.copyProperties(branchSession,branchSessionVONew);

                branchSessionVONew.setBranchType(branchSession.getBranchType().name());
                branchSessionVONew.setStatus(branchSession.getStatus().getCode());

                branchSessionVONew.setGmtCreate(null);
                branchSessionVONew.setGmtModified(null);

                branchSessionVOs.add(branchSessionVONew);
            }
        }
        return branchSessionVOs;
    }

}
