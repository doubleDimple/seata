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
import io.seata.core.console.result.PageResult;

import java.util.ArrayList;

public class PageTest {


    public static void main(String[] args) {
            ArrayList<String> strings = new ArrayList<>();

            PageResult<String> success = PageResult.success(strings, strings.size(), 2, 2);

            System.out.println(success);

    }
}
