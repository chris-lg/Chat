package com.pzf.liaotian.bean.album;

import java.util.List;


/**
 * @author:liu ge
 * @createTime:2015年5月18日
 *  @desc:相册数据体
 */
public class ImageBucket extends BaseData{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int count = 0;
    public String bucketName;
    public List<ImageItem> imageList;

}

