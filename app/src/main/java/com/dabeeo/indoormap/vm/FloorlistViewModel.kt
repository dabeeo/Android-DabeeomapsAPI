/**
 * Created by Eddie J on 2020. 2. 21
 * Copyright (c) 2020 DABEEO All rights reserved.
 * This software is the confidential and proprietary information of DABEEO.
 * You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered into with DABEEO.
 **/

package com.dabeeo.indoormap.vm

import androidx.lifecycle.MutableLiveData
import com.dabeeo.imsdk.model.common.FloorInfo
import com.dabeeo.indoormap.BaseViewModel


class FloorlistViewModel : BaseViewModel() {
    var mapList = MutableLiveData<List<FloorInfo>>()
    override fun init() {

    }
}