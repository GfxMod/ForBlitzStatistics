package ru.forblitz.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class TanksFragment : Fragment() {

    // TODO: подозрительно, что у всех фрагментов у вас пустые классы
    // Это значит, что всю логику вы делаете где-то в других местах
    // А фрагменты - вполне самостоятельные, им можно соответствующие методы добавить

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tanks, container, false)
    }

}