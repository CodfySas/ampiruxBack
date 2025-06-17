package com.osia.ampirux.service.client

import com.osia.ampirux.dto.client.v1.ClientDto
import com.osia.ampirux.dto.client.v1.ClientRequest
import com.osia.ampirux.model.Client
import com.osia.ampirux.service.common.CommonService

interface ClientService : CommonService<Client, ClientDto, ClientRequest>
