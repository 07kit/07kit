package com.kit.api;

import com.kit.api.collection.queries.ClanChatMemberQuery;
import com.kit.api.wrappers.ClanChatMember;
import com.kit.api.collection.queries.ClanChatMemberQuery;
import com.kit.api.wrappers.ClanChatMember;

import java.util.List;

public interface ClanChat {

    List<ClanChatMember> getAllMembers();

    ClanChatMemberQuery find();
}
