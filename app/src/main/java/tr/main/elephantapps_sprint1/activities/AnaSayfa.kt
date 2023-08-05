package tr.main.elephantapps_sprint1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tr.main.elephantapps_sprint1.R
import tr.main.elephantapps_sprint1.databinding.ActivityAnaSayfaBinding

class AnaSayfa : BaseActivity() {

    private lateinit var binding : ActivityAnaSayfaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAnaSayfaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}