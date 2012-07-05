package weibo4j.examples.favorites;

import weibo4j.Favorite;
import weibo4j.Weibo;
import weibo4j.examples.Log;
import weibo4j.model.WeiboException;

public class DestroyFavoritesTagsBatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String access_token = args[0];
		Weibo weibo = new Weibo();
		weibo.setToken(access_token);
		Favorite fm = new Favorite();
		boolean result = false;
		String ids = args[1];
		try {
			result = fm.destroyFavoritesTagsBatch(ids);
			Log.logInfo(String.valueOf(result));
		} catch (WeiboException e) {

			e.printStackTrace();
		}
	}

}