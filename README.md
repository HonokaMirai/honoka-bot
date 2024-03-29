# honoka-bot

&#x9;本项目依赖于[mirai](https://github.com/mamoe/mirai)、[mirai-console-loader](https://github.com/iTXTech/mirai-console-loader)项目开发

## 0.准备事项
*   根据mirai-console-loader教程安装好必要插件
*   将本插件（jar包）放进{}/plugins/文件夹中
*   Linux代理软件使用[V2rayA](https://github.com/v2rayA/v2rayA)项目
*   不使用spring，整合了mybatis、druid数据源、mybatis-plus，可以接入数据库
## 1.功能列表
*   `接入数据库`，整合mybatis、durid数据源、mybatis-plus
*   `音频使用silk格式`，支持MP3格式转换成silk格式
*   `自动解析QQ小程序`格式（B站转发、微博转发等）、自动解析bilibili、youtube地址视频信息
*   `/c` 指令，调用OpenAI-GPTAPI接口，进行对话
*   `/点歌` 指令，从网易云音乐中取对应的歌曲
## 2.功能详情

*   `mirai-data配置`
    *
*   `数据库相关配置`
    *   接入mybatis

    *   接入数据源

    *   接入mybatis-plus插件

*   `silk格式QQ语音转换`
    *   使用[silk4j](https://github.com/mzdluo123/silk4j)提供的工具类
*   `自动解析QQ小程序`
    *   `解析小程序`: 使用mirai框架进行解析
![image](https://github.com/HonokaMirai/honoka-bot/assets/67139503/4acafe7e-ac70-42db-82ce-6eaa17ac6b1b)

    *   `解析B站链接`: 使用松鼠api进行解析

    *   `解析youtube视频链接`: 使用谷歌原生api进行解析
![image](https://github.com/HonokaMirai/honoka-bot/assets/67139503/d030da43-f42e-46ad-a24b-9b2a4101b18f)


*   `/c`：使用gpt-api对话
    *   `/c [prompt]`：进行单次对话
![image](https://github.com/HonokaMirai/honoka-bot/assets/67139503/46397f96-ab91-4c7d-a00d-1ffae8be4a25)

    *   `/c sys [prompt]`：进行多次对话的system角色prompt设置
![image](https://github.com/HonokaMirai/honoka-bot/assets/67139503/cc995b8a-d332-4bf8-8c41-b361d4b0c9bf)

    *   `/c t [prompt]`：进行多次对话

    *   `/c end`：清空多次对话记录

    *   权限配置

*   `点歌`
    *   `点歌 [关键词]`：使用松鼠api提供的API接口，通过关键词搜索网易云音乐，取搜索结果第一条，转换成语音消息发送出来

    *   权限配置


