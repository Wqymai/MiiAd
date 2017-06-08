package com.mg.others.utils;


import com.mg.others.model.AdModel;
import com.mg.others.model.AdReport;
import com.mg.others.ooa.AdError;
import com.mg.others.ooa.MConstant;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AdParser {
//    public static final String testJson = "{\n" +
//            "resultCode: 1,\n" +
//            "msg: \"\",\n" +
//            "data: [\n" +
//            "{\n" +
//            "aid: \"21\",\n" +
//            "name: \"mname\",\n" +
//            "title: \"mtitle\",\n" +
//            "desc: \"mtitle\",\n" +
//            "img: \"http://oimagec7.ydstatic.com/image?id=6219066672924374315&product=adpublish&w=640&h=960\",\n" +
//            "pk: \"\",\n" +
//            "category: \"0\",\n" +
//            "fileSize: 0,\n" +
//            "type: 1,\n" +
//            "icon: \"0\",\n" +
//            "url: \"http://m.baidu.com/mobads.php?0s0000jmoQhdo17qOsyqu70EzwImYSwqXpd9rBMXAjO7g1Qy7hiSMg7jknILQWRYFXd5TbDf_KBVV8FCtE-6QcZWE-vMqKOOpkyOGbeg-fTt-IHPs_D1b0c_ucxhHDCVq5fcOTR.DR_ipEQ2q-D7LeqXyexEStaknplvUqMuvnt_rrumuCyn-XzEcC0.mgK85ymkmyc3Pjw90APV5HDznjf0myYqn0KWTAnqn6KVIjYz0AP1UWY3PWmsPjmsnWTsPjmdnHD0Tv3qrjmvnjfvnjcLnjfvPHDkQHD3rWR4rWnvrWckrhckrW7B0Aq15HD0uANv5Rk-UhqvUzdQPH0VIjR0TA-b5Hc0mv-b5H00mv6q0A4-IjYs0ZIGuWYk0ZKzujdzTLK_mgPC0A4Y5Hc0pywhmHY0TA_q0AwWgLP1TA-b5Hc0uAPxIZ-suHYs0AwWgvu85fKbmdqhUMcqn0KbmdqYUHYs0AwWgLwd5HcvPHD4njR0uAPxIAfqn0KbmdqdThsq0AwWgvPC5fKbmdqbTLKGujYv0AwWgvw-Ih-WuNqGujYz0AwWgvFzmy4b5yk-UhqvUsKbmdqsTh-WuNqGujY30AwWgvuGTMPYgLwzmyw-5HcY0AwWgLP-mvq8u7qYTh7buHYzPj0k0AwWgv4-I7qYXgK-gv-b5HD0uAPxmgwEUNqGujYs0AwWgvkBTdqGujYs0AwWgvkBTdqYXgK-5H00uAPxpgwxpyfqn0Kbmdq9u7q15ymLnWmkm1u9PyuhPHFBmhc0uAPxULPxpyfqnBYdQW00uAPxUyNbpy7xIZ-suHYs0AwWgLP-mdqbUvd9py3q0AwWgvFdTv-8ugP1gv-b5H00uAPxuA-1IZFGmLwxpyfqn0KbmdqWIy-b5fKbmdtkTLwxIAqspynqn0KbmdtzUhwxIAqspynqn0Kbmdt1ThwxIAqspynqn0Kbmdq9uAP_mgP15Hfv0AwWgvI-Uhw-TWYdnW0L0AwWgv7MuHYdnH0z0AwWgvd9TMFGmyI-5Hn3PfKbmdqhpgF1I7qGIjYs0AwWgLP-mvq8u7qGIjYs0AwWgv7sTAkGTLwxpyfqn0Kbmdq9TZKWmgw-gv-b5H00uAPxIZFGuvI-T-qGI7qYmyI15fKbmdq1uAVxIWYY0AwWgLN1ugFxIZ-suHYs0AwWgLF-TNqspvTq0AwWgvI-UdqsThqv5Hc0uAPxuvNEgvPGIZbqPj030AwWgv7WIA-vuNq9TZ0qn0Kbmdq9mLwGIhNxmv7YuyIETMbqn0Kbmdq9uZ6qnfKbmdqL5HTzn0KbmdqC5HDzrj00uAPxIZws5Hn0myPWIjd9TAb0ULPxUWd9UhwzUv-bQHR8n0K9mLwY5HD0uAP8IjYknj00mLPVIjYs0AFMIZ0qn0KsuMwz5H00mvkomgwYTjdspgK-0Z7YUHYkPjm3n10zn1bk0AN3mv9YTjYs0APCUyqb5HD0TvPCUyqb5HD0TLKzpyP-5HDYn0KVmgwWp7qWUvw-5H00TM7dmHYeQH_e0ZI9UAkxTAqGUMfqn0K9UhwzUv-bgv-b5H0knWnYPHmLrj-9mhPbuym0TA9E5H00ug9s5gIsQZwEQAwJQHD_Xyn_IZn_uMn_UAf_Tg6VnBkhmzksXBs0UhNLgvN3T7qGuZnq0ZI9T7qWUvqopyRq0APzm1YznHc100\",\n" +
//            "et: 1468302751,\n" +
//            "cb: {\n" +
//            "pv: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=3ycHgGv5x7HhVQeBPWbmVFAy+8SDMWKbQyJ9pKMP-z7vc5fmlVYU4XxLBU6wPJSFiWtuQjq9PJQZkjX7EBs2-vMp1UKlFApBCfQ-KzKrAUhtToNM+DaYgfY0DxM7TSVp0Y80fMN+qjyoc5l2SRLT+Tq5k0qwbYQfTTYVMFStFUNA!UcjR3uCi2NFs06LVNjGPONnVTE5Iz7yJ7QCN0q7VAsBcwJHznohRDEwMnPcfTaLEno0GaNFTThxEp74VDOjEDDmk7SxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT0xJnBvc1R5cGU9Mg==Jb2\"\n" +
//            "],\n" +
//            "c: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=xcW!G0ZxBQhEZebyWS2VuHyAeSM+W7WQl39i-M42zQkcasmYsYIDXn!BlpwurS2cWBmQiv9nwQmgj0SE-92MTMxeU6HFgXBPJQP!zf-AHKt84NpiDgbgSq0K9MkqSUv0hv06+NA1j+5c6K2hXL!bTlAkoBwrcQa3THFMfmtrKNejUunRNfCMiN++0uWVi-GGJNCZTwKINyymIQlr0YNVdABZMJ8Nn4oRuCws6PwnT9KEY-0rhNyITipEH24QFO3bDqPkP7x0MTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT0yJnBvc1R5cGU9Mg==g1N\"\n" +
//            "],\n" +
//            "ds: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=cEcdOGmVxFThLoel6WYzVt5yDTSKQWVBQy6918Mp2zAMcbGmG1YufXryBddwzvSVuWh8QrL9KPQ0ajSbEgQ2coMJeU0KFyyBy3QHbzvPAfFtNpNJkDRmgqL0ZcMuhSM60EV0e8NbRjmJcsW2UGLViTqRk7swtfQS+TWdM+Et8+NyAUMBRQICZDNzx0rkVsMGNhNeoTi7IyIy4xQWH03jVPUBHKJj1nenRsQwUWP70TcaEX40HINF!TMWEAW46SOUjDQgk6PxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT0zJnBvc1R5cGU9Mg==JY2\"\n" +
//            "],\n" +
//            "de: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=cbhGgyxtdh27e5yWpXVH8yuzSIfWe8QFN9zBM+ZzUKcQ7mIIYopXiLB4jw!LSw!WpnQYc99gQjMjGbE1n2OCMv8U4mFeoBB0Ql!zVdAHit9eN5QDHkgE20HHMklSbP0gO0Y9NzYj9hcZg2VyLULTStkInwPfQU9T2XM6ytU4N!bU!ARjqCJfNIa0lMVpdGzhNDKTELItVyr+QVU08sVvGBe+JUWn+eRzMwukPhyT4aEyJ0jiNQdTTgE7u4LvOYuD--k+rxKtMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT00JnBvc1R5cGU9Mg==0Tt\"\n" +
//            "],\n" +
//            "is: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=15cxHGIBx2fh2GeozWHeV1hyiRSn0WwMQgV9SSMmszdfc02mE2YuPXULBdKwVBSNxWAMQ-39zqQBRjc6EkW2eSM+hUtdF0dBGtQkFzrKAaAtINNJlDXtgaH0BFMoMSHh03g0b+Nrsjb9c!s2FeLJDT3fkDLw9aQ2nTESMxVtY6NcwUgjRWMC0rN0z0dYVYFGKbNt9TMLIigy6oQ!y08kVCmBVwJZvnW0R9RwAFP9VTUOEa90-mNyaTBNEbJ4f!OoMDuCkFXxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT01JnBvc1R5cGU9Mg==Jm2\"\n" +
//            "],\n" +
//            "ie: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=6cXvG99xByhbFeLgW2VVYVyL8SK2WuqQ9o9i2MyhzbMcwzmT1YAqXmeB!bwLUSQ1WbHQ5N9!FQ8OjM5ENJ2DHMPxUpMFOEBvbQpvzG5AFBtzeN7iD9BggV0iWMXrSqK0TX0RpNitjygcKQ2MeLDPTHDkc7wqkQfkTl!MCFtOKNKWUTVRRyC30Nic0f8VUsGYPNCHTgLIlkyeSQd40LZVYaBWvJ8+nUqR-MwqEPYUTE-Ez!00dNpGTfTEOE4ToO0yDpskk8xJMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT02JnBvc1R5cGU9Mg==v1P\"\n" +
//            "],\n" +
//            "a: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=a!c07G!FxFwhWxe2vWB8VSay33SG7Wc0Q+X9ZXMqezYac38meoYz2XDjBYAw+qSSIWHJQBG9qJQ7sjTFEo82vPMVnU4lFDwBN3QjKzZ6AWYt3xNG3Dqig-i0cjMtFSKA0DC0-9NS6jm9cqM2yCLCXT9rkYqw54Qv0T9IMDotZSNkpU-eRioCNANA80tnVvqG1KNUbToYI3dyNcQNh0coV4gBaDJiznXsR03wYvP08TR0ERj0boN4YTtZEjX46OOXgDxak1xxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT03JnBvc1R5cGU9Mg==dX2\"\n" +
//            "]\n" +
//            "}\n" +
//            "}\n" +
//            "]\n" +
//            "}";
//
//    public static final String testJson_noUrl = "{\n" +
//            "resultCode: 1,\n" +
//            "msg: \"\",\n" +
//            "data: [\n" +
//            "{\n" +
//            "aid: \"21\",\n" +
//            "name: \"mname\",\n" +
//            "title: \"mtitle\",\n" +
//            "desc: \"mtitle\",\n" +
//            "img: \"http://ubmcmm.baidustatic.com/media/v1/0f0007MGSuTmiAOKG7dqrf.jpg\",\n" +
//            "pk: \"\",\n" +
//            "category: \"0\",\n" +
//            "fileSize: 0,\n" +
//            "type: 3,\n" +
//            "icon: \"0\",\n" +
//            "url: \"http://www.hupu.com\",\n" +
//            "et: 1468302751,\n" +
//            "cb: {\n" +
//            "pv: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=3ycHgGv5x7HhVQeBPWbmVFAy+8SDMWKbQyJ9pKMP-z7vc5fmlVYU4XxLBU6wPJSFiWtuQjq9PJQZkjX7EBs2-vMp1UKlFApBCfQ-KzKrAUhtToNM+DaYgfY0DxM7TSVp0Y80fMN+qjyoc5l2SRLT+Tq5k0qwbYQfTTYVMFStFUNA!UcjR3uCi2NFs06LVNjGPONnVTE5Iz7yJ7QCN0q7VAsBcwJHznohRDEwMnPcfTaLEno0GaNFTThxEp74VDOjEDDmk7SxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT0xJnBvc1R5cGU9Mg==Jb2\"\n" +
//            "],\n" +
//            "c: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=xcW!G0ZxBQhEZebyWS2VuHyAeSM+W7WQl39i-M42zQkcasmYsYIDXn!BlpwurS2cWBmQiv9nwQmgj0SE-92MTMxeU6HFgXBPJQP!zf-AHKt84NpiDgbgSq0K9MkqSUv0hv06+NA1j+5c6K2hXL!bTlAkoBwrcQa3THFMfmtrKNejUunRNfCMiN++0uWVi-GGJNCZTwKINyymIQlr0YNVdABZMJ8Nn4oRuCws6PwnT9KEY-0rhNyITipEH24QFO3bDqPkP7x0MTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT0yJnBvc1R5cGU9Mg==g1N\"\n" +
//            "],\n" +
//            "ds: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=cEcdOGmVxFThLoel6WYzVt5yDTSKQWVBQy6918Mp2zAMcbGmG1YufXryBddwzvSVuWh8QrL9KPQ0ajSbEgQ2coMJeU0KFyyBy3QHbzvPAfFtNpNJkDRmgqL0ZcMuhSM60EV0e8NbRjmJcsW2UGLViTqRk7swtfQS+TWdM+Et8+NyAUMBRQICZDNzx0rkVsMGNhNeoTi7IyIy4xQWH03jVPUBHKJj1nenRsQwUWP70TcaEX40HINF!TMWEAW46SOUjDQgk6PxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT0zJnBvc1R5cGU9Mg==JY2\"\n" +
//            "],\n" +
//            "de: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=cbhGgyxtdh27e5yWpXVH8yuzSIfWe8QFN9zBM+ZzUKcQ7mIIYopXiLB4jw!LSw!WpnQYc99gQjMjGbE1n2OCMv8U4mFeoBB0Ql!zVdAHit9eN5QDHkgE20HHMklSbP0gO0Y9NzYj9hcZg2VyLULTStkInwPfQU9T2XM6ytU4N!bU!ARjqCJfNIa0lMVpdGzhNDKTELItVyr+QVU08sVvGBe+JUWn+eRzMwukPhyT4aEyJ0jiNQdTTgE7u4LvOYuD--k+rxKtMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT00JnBvc1R5cGU9Mg==0Tt\"\n" +
//            "],\n" +
//            "is: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=15cxHGIBx2fh2GeozWHeV1hyiRSn0WwMQgV9SSMmszdfc02mE2YuPXULBdKwVBSNxWAMQ-39zqQBRjc6EkW2eSM+hUtdF0dBGtQkFzrKAaAtINNJlDXtgaH0BFMoMSHh03g0b+Nrsjb9c!s2FeLJDT3fkDLw9aQ2nTESMxVtY6NcwUgjRWMC0rN0z0dYVYFGKbNt9TMLIigy6oQ!y08kVCmBVwJZvnW0R9RwAFP9VTUOEa90-mNyaTBNEbJ4f!OoMDuCkFXxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT01JnBvc1R5cGU9Mg==Jm2\"\n" +
//            "],\n" +
//            "ie: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=6cXvG99xByhbFeLgW2VVYVyL8SK2WuqQ9o9i2MyhzbMcwzmT1YAqXmeB!bwLUSQ1WbHQ5N9!FQ8OjM5ENJ2DHMPxUpMFOEBvbQpvzG5AFBtzeN7iD9BggV0iWMXrSqK0TX0RpNitjygcKQ2MeLDPTHDkc7wqkQfkTl!MCFtOKNKWUTVRRyC30Nic0f8VUsGYPNCHTgLIlkyeSQd40LZVYaBWvJ8+nUqR-MwqEPYUTE-Ez!00dNpGTfTEOE4ToO0yDpskk8xJMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT02JnBvc1R5cGU9Mg==v1P\"\n" +
//            "],\n" +
//            "a: [\n" +
//            "\"http://vs.maimob.net/index.php/VSAPI/EP?m=a!c07G!FxFwhWxe2vWB8VSay33SG7Wc0Q+X9ZXMqezYac38meoYz2XDjBYAw+qSSIWHJQBG9qJQ7sjTFEo82vPMVnU4lFDwBN3QjKzZ6AWYt3xNG3Dqig-i0cjMtFSKA0DC0-9NS6jm9cqM2yCLCXT9rkYqw54Qv0T9IMDotZSNkpU-eRioCNANA80tnVvqG1KNUbToYI3dyNcQNh0coV4gBaDJiznXsR03wYvP08TR0ERj0boN4YTtZEjX46OOXgDxak1xxMTkmY2FtcGFpZ249MjUmYWlkPTIxJmZyb209MTEmYXBpQXBwSWQ9JmFwaVRyYW5zPSZhcGlQYWNrYWdlTmFtZT0mdHlwZT03JnBvc1R5cGU9Mg==dX2\"\n" +
//            "]\n" +
//            "}\n" +
//            "}\n" +
//            "]\n" +
//            "}";

    public static final String RESULTCODE = "resultCode";
    public static final String MSG = "msg";
    public static final String DATA = "data";

    public static final String AID = "aid";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String IMG = "img";

    public static final String URL = "url";
    public static final String ICON = "icon";
    public static final String CATEGORY = "category";
    public static final String PK = "pk";
    public static final String FILESIZE = "fileSize";
    public static final String TYPE = "type";
    public static final String PAGE="page";
    public static final String PT = "pt";
    public static final String ET = "et";
    public static final String CB = "cb";
    public static final String PV = "pv";
    public static final String C = "c";
    public static final String DS = "ds";
    public static final String DE = "de";
    public static final String IS = "is";
    public static final String IE = "ie";
    public static final String A = "a";


    public static List<AdModel> parseAd(String response){
//        LogUtils.i(MConstant.TAG,"广告请求返回="+response);
//        LogUtils.i(MConstant.TAG, LocalLogConstants.AD_RESPONSE+response);
        List<AdModel> adModels = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int resultCode = jsonObject.optInt(RESULTCODE);
            if (resultCode == MConstant.SUC_CODE){
                adModels = new ArrayList<>();
                JSONArray array_data = jsonObject.optJSONArray(DATA);
                for (int i = 0; i < array_data.length(); i++) {
                    AdModel ad = new AdModel();
                    JSONObject object_ad = (JSONObject) array_data.opt(i);
                    String aid = (String) object_ad.opt(AID);
                    String name = (String) object_ad.opt(NAME);
                    String title = (String) object_ad.opt(TITLE);
                    String desc = object_ad.optString(DESC);
                    String img = object_ad.optString(IMG);
                    String pk = object_ad.optString(PK);
                    String category = object_ad.optString(CATEGORY);
                    int type = object_ad.optInt(TYPE);
                    String page=object_ad.optString(PAGE);
                    String icon = object_ad.optString(ICON);
                    String url = object_ad.optString(URL);
                    int pt = object_ad.optInt(PT);
                    int et = object_ad.optInt(ET);

                    JSONObject object_cb = object_ad.optJSONObject(CB);
                    JSONArray pv = object_cb.optJSONArray(PV);
                    JSONArray c  = object_cb.optJSONArray(C);
                    JSONArray ds = object_cb.optJSONArray(DS);
                    JSONArray de = object_cb.optJSONArray(DE);
                    JSONArray ie = object_cb.optJSONArray(IE);
                    JSONArray a  = object_cb.optJSONArray(A);

                    String[] array_pv = parserCb(pv);
                    String[] array_c  = parserCb(c);
                    String[] array_ds = parserCb(ds);
                    String[] array_de = parserCb(de);
                    String[] array_ie = parserCb(ie);
                    String[] array_a  = parserCb(a);

                    AdReport adReport = new AdReport();
                    adReport.setUrlClick(array_c);
                    adReport.setUrlDownloadComplete(array_de);
                    adReport.setUrlDownloadStart(array_ds);
                    adReport.setUrlInstallComplete(array_ie);
                    adReport.setUrlShow(array_pv);
                    adReport.setUrlOpen(array_a);

                    ad.setUrl(url);
                    ad.setCategory(category);
                    ad.setDesc(desc);
                    ad.setIcon(icon);
                    ad.setId(aid);
                    ad.setImage(img);
                    ad.setName(name);
                    ad.setPkName(pk);
                    ad.setType(type);
                    ad.setPage(page);
                    ad.setTitle(title);
                    ad.setReportBean(adReport);
                    ad.setPt(pt);
                    ad.setEt(et);
                    adModels.add(ad);
                }
            }else {
                LogUtils.i(MConstant.TAG,new AdError(AdError.ERROR_CODE_INVALID_REQUEST, jsonObject.optString(MSG)).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            adModels = null;
        }
        return adModels;
    }

    public static String[] parserCb(JSONArray array){
        String[] strings = new String[array.length()];
        for (int i = 0; i < array.length(); i++) {
            try {
                strings[i] = array.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return strings;
    }

}
