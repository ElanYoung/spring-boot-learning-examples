# Spring Boot 自定义 Banner

## 创建文件

首先在 Spring Boot 项目 `resources` 目录下新建 `banner.txt` 文本文件；

![创建](https://img-blog.csdnimg.cn/img_convert/5e7764cdd6cb82986bc4fa0da557540d.png)

## 粘贴文本

其次将启动 Banner 粘贴到此文本文件中，启动项目即可。

```
${AnsiColor.BRIGHT_GREEN}$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
${AnsiColor.BRIGHT_YELLOW}$$                                _.ooOoo._                               $$
${AnsiColor.BRIGHT_RED}$$                               o888888888o                              $$
${AnsiColor.BRIGHT_CYAN}$$                               88"  .  "88                              $$
${AnsiColor.BRIGHT_MAGENTA}$$                               (|  ^_^  |)                              $$
${AnsiColor.BRIGHT_GREEN}$$                               O\   =   /O                              $$
${AnsiColor.BRIGHT_RED}$$                            ____/`-----'\____                           $$
${AnsiColor.BRIGHT_CYAN}$$                          .'  \\|       |$$  `.                         $$
${AnsiColor.BRIGHT_MAGENTA}$$                         /  \\|||   :   |||$$  \                        $$
${AnsiColor.BRIGHT_GREEN}$$                        /  _|||||  -:-  |||||-  \                       $$
${AnsiColor.BRIGHT_YELLOW}$$                        |   | \\\   -   $$/ |   |                       $$
${AnsiColor.BRIGHT_GREEN}$$                        | \_|  ''\-----/''  |   |                       $$
${AnsiColor.BRIGHT_YELLOW}$$                        \  .-\___  `-`  ____/-. /                       $$
${AnsiColor.BRIGHT_CYAN}$$                      ___`. .'   /--.--\   `. . ___                     $$
${AnsiColor.BRIGHT_RED}$$                    ."" '<  `.____\_<|>_/____.'  >'"".                  $$
${AnsiColor.BRIGHT_GREEN}$$                  | | :  `- \`.;`.\ _ /``;.`/ - ` : | |                 $$
${AnsiColor.BRIGHT_YELLOW}$$                  \  \ `-.   \_ ___\ /___ _/   .-` /  /                 $$
${AnsiColor.BRIGHT_CYAN}$$            ========`-.____`-.____\_____/____.-`____.-'========         $$
${AnsiColor.BRIGHT_MAGENTA}$$                                  `=---='                               $$
${AnsiColor.BRIGHT_YELLOW}$$            ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        $$
${AnsiColor.BRIGHT_GREEN}$$                     佛祖保佑          永无BUG         永不修改         $$
${AnsiColor.BRIGHT_YELLOW}$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
```

![成功](https://img-blog.csdnimg.cn/img_convert/e3b4372106558f8e358879284933679b.png)

## 在线制作网站

- http://patorjk.com/software/taag
- https://www.bootschool.net/ascii
- http://www.network-science.de/ascii
- https://www.degraeve.com/img2txt.php
