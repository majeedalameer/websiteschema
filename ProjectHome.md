This project propose a method to discover the schema of a website, to implement the numerical analysis on web pages. The final goal is to implement the Unsupervised Learning on Web Information Extraction.

综合目前对信息抽取技术的分析，本文借鉴在搜索引擎中，将文档集转换成向量空间的思路上，提出将网页布局数值化的方法，用以实现网页信息抽取的自动化。在本文构建的向量空间中，DOM树中的每一个XPath就是一个维度。通过构建向量空间，并为相同网站的不同页面进行聚类，通过聚类分析出相似的网页，然后采用目前一些已经较为成熟的方法，实现自动化抽取。本文介绍了网页布局的数值化方法，实现了对网易新闻的聚类分析，以及介绍了一个在此基础之上的信息抽取系统的简单实现。通过比较实验结果，证明了基于网页布局的向量空间，为实现无监督的机器学习提供了基本、有效的分析数据。另外作者认为其更重要的一个作用，是提示了大家不能忽略对目标网站页面类型的分析，其应该成为信息抽取的基本步骤，处于信息抽取的预处理环节中。

2011-08-30，websiteschema集成了WebRenderer做为采集程序的Web Renderer，虽然有点绕口。同时websiteschema实现VIPS的功能，并正在完善中。