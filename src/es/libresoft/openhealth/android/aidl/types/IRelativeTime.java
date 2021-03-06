/*
Copyright (C) 2008-2011 GSyC/LibreSoft, Universidad Rey Juan Carlos.

Author: Jose Antonio Santos Cadenas <jcaden@libresoft.es>
Author: Santiago Carot-Nemesio <scarot@libresoft.es>

This program is a (FLOS) free libre and open source implementation
of a multiplatform manager device written in java according to the
ISO/IEEE 11073-20601. Manager application is designed to work in
DalvikVM over android platform.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package es.libresoft.openhealth.android.aidl.types;

import android.os.Parcel;
import android.os.Parcelable;

public class IRelativeTime implements Parcelable {
	private long value;

	public static final Parcelable.Creator<IRelativeTime> CREATOR =
			new Parcelable.Creator<IRelativeTime>() {
		public IRelativeTime createFromParcel(Parcel in) {
			return new IRelativeTime(in);
		}

		public IRelativeTime[] newArray(int size) {
			return new IRelativeTime[size];
		}
	};

	public IRelativeTime () {

	}

	public IRelativeTime (long value) {
		this.value = value;
	}

	private IRelativeTime (Parcel in) {
		value = in.readLong();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(value);
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IRelativeTime))
			return false;
		IRelativeTime other = (IRelativeTime) obj;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new Long(value).toString();
	}

}
