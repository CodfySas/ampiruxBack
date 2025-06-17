package com.osia.ampirux.service.message

import com.osia.ampirux.dto.message.v1.MessageDto
import com.osia.ampirux.dto.message.v1.MessageRequest
import com.osia.ampirux.model.Message
import com.osia.ampirux.service.common.CommonService

interface MessageService : CommonService<Message, MessageDto, MessageRequest>
