package com.mg.others.http;

public interface UpgradeListener {
	/**
	 * check if there is new version.
	 *
	 *            , true if there is new version.
	 * @param serverVersionCode
	 *            , server version code
	 * @param serverVersion
	 *            , server version string
	 * @param serverVersionDescription
	 *            , server version description
	 */
	public void onNewVersion(boolean forceUpgrade, int serverVersionCode,
							 String serverVersion, String serverVersionDescription, String
									 downloadUrl);

	public void onNoNewVersion();

	/**
	 * check failed due to network problems.
	 */
	public void onCheckFailed();

}
