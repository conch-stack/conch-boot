/*
 * Copyright [2019] [恒宇少年 - 于起宇]
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 *
 */
package com.nabob.conch.boot.autoconfigure.enhance;

import org.apache.ibatis.session.Configuration;

/**
 * Callback interface that can be customized a {@link Configuration} object generated on auto-configuration.
 * @author：恒宇少年 - 于起宇
 * <p>
 * DateTime：2019-04-25 15:16
 * Blog：http://blog.yuqiyu.com
 * WebSite：http://www.jianshu.com/u/092df3f77bca
 * Gitee：https://gitee.com/hengboy
 * GitHub：https://github.com/hengboy
 */
public interface ConfigurationCustomizer {

  /**
   * Customize the given a {@link Configuration} object.
   * @param configuration the configuration object to customize
   */
  void customize(Configuration configuration);

}
