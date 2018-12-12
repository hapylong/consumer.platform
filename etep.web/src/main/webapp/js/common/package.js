/**
 * @author 
 * @Version
 * @DateTime
 */
function $package(ns) {
	if (typeof(ns) != "string")
		return;
	ns = ns.split(".");
	var o, ni;
	for (var i = 0, len = ns.length;i < len, ni = ns[i]; i++) {
		try {
			o = (o ? (o[ni] = o[ni] || {}) : (eval(ni + "=" + ni + "||{}")))
		} catch (e) {
			o = eval(ni + "={}")
		}
	}
}
